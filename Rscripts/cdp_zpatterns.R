cdp_check = function(){
  if(!exists('X')) return("No data has been selected")
  
  cond1 = cdp_check_compositional(X, zero_allowed = TRUE)
  if(!is.null(cond1)) return(cond1)
  
  cond2 = min(X) == 0
  if(!cond2) return("No zero detected")
}
cdp_analysis = function(){
  graphname <- sprintf("%s.svg", tempfile())
  
  svg(graphname, width = PLOT_WIDTH, height = PLOT_HEIGTH)
  sortida <- capture.output(zPat <- zCompositions::zPatterns(X,label=0,show.means=V1,bar.labels=V2))
  dev.off()
  
  new_data = list()
  if(V3) new_data = data.frame('Patt.ID' = zPat)
  
  # Output
  list(
    'text' = sortida,
    'dataframe' = list(),
    'graph' = c(graphname),
    'new_data' = new_data
  )
  
}