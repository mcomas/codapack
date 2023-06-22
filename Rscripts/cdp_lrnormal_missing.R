cdp_check = function(){
  if(!exists('X')) return("No data has been selected")
  
  cond1 = cdp_check_compositional(X, na_allowed = TRUE)
  if(!is.null(cond1)) return(cond1)
  
}
cdp_analysis = function(){
  Ximp <- zCompositions::lrEM(X,label=NA,imp.missing=TRUE,ini.cov="complete.obs",rob=V1)
  names(Ximp) = paste0("z.", colnames(X))
  
  # Ooutput
  list(
    'text' = "",
    'dataframe' = list(),
    'graph' = list(),
    'new_data' = data.frame(Ximp)
  )
}