cdp_check = function(){
  if(!exists('X')) return("First composition has not been selected")
  if(!exists('Y')) return("Second composition has not been selected")
  
  cond1 = cdp_check_compositional(X)
  if(!is.null(cond1)) return(cond1)
  
  cond2 = cdp_check_compositional(Y)
  if(!is.null(cond2)) return(cond2)
  
  if(nrow(X) != nrow(Y)) return("Both compositions must have the same number of observations")
  if(ncol(X) != ncol(Y)) return("Both compositions must have the same number of parts")
  
  p = ncol(X) - 1
  if(p < 1) return("At least two parts should be selected")
  if(p > 1 && nrow(X) <= p) return("More observations than OLR coordinates are needed")
  
}

cdp_analysis = function(){
  # save.image("Rscripts/cdp_paired_perturbation.RData")
  
  B = as.matrix(coda.base::ilr_basis(ncol(X)))
  HX = coda.base::coordinates(X, basis = B)
  HY = coda.base::coordinates(Y, basis = B)
  
  colnames(HX) = paste0('olr.', 1:ncol(HX))
  colnames(HY) = paste0('olr.', 1:ncol(HY))
  
  D = HY - HX
  colnames(D) = paste0('d.', colnames(HX))
  
  n = nrow(D)
  p = ncol(D)
  dbar = colMeans(D)
  paired_t = function(x){
    fallback = function(){
      return(list(
        estimate = c("mean of x" = mean(x)),
        statistic = c("t" = NA_real_),
        parameter = c("df" = length(x) - 1),
        p.value = ifelse(abs(mean(x)) < .Machine$double.eps^0.5, 1, 0)
      ))
    }
    if(sd(x) < .Machine$double.eps^0.5) fallback()
    else tryCatch(t.test(x, mu = 0), error = function(e) fallback())
  }
  
  text_output = c()
  
  if(p == 1){
    TT = paired_t(D[,1])
    overall = data.frame(
      "Test" = "Paired t-test",
      "Statistic" = unname(TT$statistic),
      "df1" = unname(TT$parameter),
      "df2" = NA_real_,
      "p.value" = TT$p.value,
      check.names = FALSE
    )
    text_output = c(text_output,
                    "Paired t-test:",
                    capture.output(print(overall, row.names = FALSE)))
  }else{
    S = cov(D)
    if(qr(S)$rank < p){
      text_output = c(text_output,
                      "Hotelling T-squared test could not be computed because the covariance matrix is singular.")
      overall = data.frame(
        "Test" = "Hotelling T-squared",
        "Statistic" = NA_real_,
        "df1" = p,
        "df2" = n - p,
        "p.value" = NA_real_,
        check.names = FALSE
      )
    }else{
      T2 = as.numeric(n * t(dbar) %*% solve(S, dbar))
      Fstat = (n - p) * T2 / (p * (n - 1))
      pvalue = pf(Fstat, p, n - p, lower.tail = FALSE)
      overall = data.frame(
        "Test" = "Hotelling T-squared",
        "Statistic" = Fstat,
        "df1" = p,
        "df2" = n - p,
        "p.value" = pvalue,
        check.names = FALSE
      )
      text_output = c(text_output,
                      sprintf("Hotelling T-squared = %s", formatC(T2)),
                      "F approximation:",
                      capture.output(print(overall, row.names = FALSE)))
    }
  }
  text_output = gsub("[‘’]", "'", text_output)
  
  new_data = list()
  if(V1){
    CD = as.data.frame(coda.base::composition(D, basis = B))
    names(CD) = paste0(colnames(X), '.pdiff')
    new_data = as.list(CD)
  }
  
  cdp_out = list(
    'text' = text_output,
    'dataframe' = list('overall' = overall),
    'graph' = list(),
    'new_data' = new_data
  )
  cdp_out
}
