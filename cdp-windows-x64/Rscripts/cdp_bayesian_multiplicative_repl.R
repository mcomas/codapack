cdp_check = function(){
  if(!exists('X')) return("No data has been selected")
  
  cond1 = cdp_check_compositional(X, zero_allowed = TRUE)
  if(!is.null(cond1)) return(cond1)
  
}
cdp_analysis = function(){
  # save.image("Rscripts/cdp_bayesian_multiplicative_repl.RData")
  Ximp = zCompositions::cmultRepl(X,label=0, method=V1, threshold=0.5, output=V2)
  names(Ximp) = paste0("z.", colnames(X))
  
  # Ooutput
  list(
    'text' = "",
    'dataframe' = list(),
    'graph' = list(),
    'new_data' = data.frame(Ximp)
  )
}