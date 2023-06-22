cdp_check = function(){
  if(!exists('X')) return("No data has been selected")
  
  cond1 = NCOL(X) == 2
  if(!cond1) return("Two categoric variables should be selected")
  
}
cdp_analysis = function(){
  graphname = sprintf("%s.pdf", tempfile())
  svg(filename = graphname, width = PLOT_WIDTH, height = PLOT_HEIGTH)
  
  plot(as.factor(as.matrix(X[,1])),as.factor(as.matrix(X[,2])), xlab=colnames(X)[1], ylab="")
  dev.off()
  
  list(
    'text' = "",
    'dataframe' = list(),
    'graph' = graphname,
    'new_data' = list())
  
}