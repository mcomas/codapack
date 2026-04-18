cdp_check = function(){
  if(!exists('X')) return("No data has been selected")
  
  cond1 = cdp_check_compositional(X)
  if(!is.null(cond1)) return(cond1)
  
}
cdp_analysis = function(){
  threshold <- V1
  H <- coda.base::coordinates(X)
  
  
  md <- mahalanobis(H, colMeans(H), cov(H))
  chsq <- pchisq(md,df=ncol(H))
  atyp <- as.integer(chsq > threshold)
  c.atyp <- ifelse(atyp == 1, "Atypical", "Non atypical")
  
  df = data.frame("Chisq" = chsq, "Atyp" = c.atyp)
  
  output = "No potential outlier"
  if(sum(atyp) > 0){
    output = sprintf("Potential outliers: %s\n", paste(which(atyp > 0), collapse = ', '))
  }
  
  list(
    'text' = output,
    'dataframe' = list(),
    'graph' = list(),
    'new_data' = df
  )
}
  
  