cdp_check = function(){  
  if(!exists('X')) return("No data has been selected")
  save.image('Rscripts/cdp_zpatterns.RData')
  if(V5 == "0") cond1 = cdp_check_compositional(X, zero_allowed = TRUE, na_allowed = TRUE)
  if(V5 == "NA") cond1 = cdp_check_compositional(X, zero_allowed = TRUE, na_allowed = TRUE)
  if(!is.null(cond1)) return(cond1)
  if(V5 == "0"){
    cond2 = min(X, na.rm=TRUE) == 0
    if(!cond2) return("No zero detected")
  }
  if(V5 == "NA"){
    cond2 = max(is.na(X) == 0)
    if(!cond2) return("No missing detected")
  }

  
}
cdp_analysis = function(){
  save.image('Rscripts/cdp_zpatterns.RData')
  graphname = NULL
  if(V4){
    graphname <- sprintf("%s.svg", tempfile())
    svg(graphname, width = PLOT_WIDTH, height = PLOT_HEIGTH)
    sortida <- capture.output(zPat <- zCompositions::zPatterns(X,label=suppressWarnings(as.numeric(V5)),show.means=V1,bar.labels=V2))
    dev.off()
  }else{
    sortida <- capture.output(zPat <- zCompositions::zPatterns(X,plot=FALSE,label=suppressWarnings(as.numeric(V5)),show.means=V1,bar.labels=V2))
  }
  
  new_data = list()
  if(V3) new_data = data.frame('Patt.ID' = zPat)
  
  # Output
  list(
    'text' = sortida,
    'dataframe' = list(),
    'graph' = graphname,
    'new_data' = new_data
  )
  
}