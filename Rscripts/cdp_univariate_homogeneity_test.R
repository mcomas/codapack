cdp_check = function(){
  if(!exists('X')) return("No data has been selected")
  if(!exists('GROUP')) return("No group has been selected")
  if(length(unique(GROUP)) <= 1) return("Two or more categories are needed")
}

cdp_analysis = function(){
  # save.image("Rscripts/cdp_univariate_homogeneity_test.RData")
  
  group_name = "GROUP"
  if(exists("GROUP_NAME")) group_name = GROUP_NAME
  
  run_bartlett = function(x, g){
    ok = !is.na(x) & !is.na(g)
    x = x[ok]
    g = droplevels(as.factor(g[ok]))
    if(length(levels(g)) <= 1) return(c(NA_real_, NA_real_, NA_real_, "Less than two non-empty groups"))
    if(any(table(g) < 2)) return(c(NA_real_, NA_real_, NA_real_, "At least two observations per group are needed"))
    
    tst = bartlett.test(x, g)
    c(unname(tst$statistic), unname(tst$parameter), tst$p.value, "")
  }
  
  run_levene = function(x, g){
    ok = !is.na(x) & !is.na(g)
    x = x[ok]
    g = droplevels(as.factor(g[ok]))
    if(length(levels(g)) <= 1) return(c(NA_real_, NA_real_, NA_real_, "Less than two non-empty groups"))
    if(any(table(g) < 2)) return(c(NA_real_, NA_real_, NA_real_, "At least two observations per group are needed"))
    
    centers = tapply(x, g, median)
    z = abs(x - centers[as.character(g)])
    A = anova(lm(z ~ g))
    c(unname(A[1, "F value"]), unname(A[1, "Df"]), unname(A[2, "Df"]), unname(A[1, "Pr(>F)"]), "")
  }
  
  if(V1 == "Bartlett"){
    RES = t(sapply(seq_len(ncol(X)), function(i) run_bartlett(X[,i], GROUP)))
    RES = data.frame(
      "Variable" = colnames(X),
      "Statistic" = as.numeric(RES[,1]),
      "df" = as.numeric(RES[,2]),
      "p.value" = as.numeric(RES[,3]),
      "Note" = RES[,4],
      check.names = FALSE
    )
    output = c("Bartlett test for homogeneity of variances",
               paste("Variables:", paste(colnames(X), collapse = ", ")),
               paste("Group:", group_name),
               "",
               capture.output(print(RES, row.names = FALSE)))
  }
  
  if(V1 == "Levene"){
    RES = t(sapply(seq_len(ncol(X)), function(i) run_levene(X[,i], GROUP)))
    RES = data.frame(
      "Variable" = colnames(X),
      "F" = as.numeric(RES[,1]),
      "df1" = as.numeric(RES[,2]),
      "df2" = as.numeric(RES[,3]),
      "p.value" = as.numeric(RES[,4]),
      "Note" = RES[,5],
      check.names = FALSE
    )
    output = c("Levene test for homogeneity of variances (median centered)",
               paste("Variables:", paste(colnames(X), collapse = ", ")),
               paste("Group:", group_name),
               "",
               capture.output(print(RES, row.names = FALSE)))
  }
  
  output = gsub("[‘’]", "'", output)
  
  list(
    'text' = output,
    'dataframe' = list('homogeneity' = RES),
    'graph' = list(),
    'new_data' = list()
  )
}
