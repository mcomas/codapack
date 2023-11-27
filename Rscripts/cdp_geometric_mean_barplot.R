cdp_check = function(){
  if(!exists('X')) return("No data has been selected")
  if(!exists('GROUP')) return("No group has been selected")
  
  cond1 = cdp_check_compositional(X)
  if(!is.null(cond1)) return(cond1)
}
cdp_analysis = function(){
  # save.image(file = 'cdp_geometric_mean_barplot.RData')
  Y = as.factor(GROUP)
  LX = log(X / rowSums(X))

  M = apply(LX, 2, function(lx) tapply(lx, Y, mean) - mean(lx))

  
  graphname = sprintf("%s.pdf", tempfile())
  svg(graphname, width = PLOT_WIDTH, height = PLOT_HEIGTH)
  if(V1 == 'groups'){
    barplot(M, beside=TRUE, col=1:nlevels(Y), ylim = range(M) * c(1.2,1.35))
    legend('top', levels(Y), horiz = TRUE, bty = 'n', fill = 1:nlevels(Y))
    abline(h = 0, lty = 1)
  }else{
    barplot(t(M), beside=TRUE, col=1:ncol(X), ylim = range(M) * c(1.2,1.35))
    legend('top', colnames(X), horiz = TRUE, bty = 'n', fill = 1:ncol(X))
    abline(h = 0, lty = 1)
  }
  dev.off()
  
  list(
    'text' = "",
    'dataframe' = list(),
    'graph' = graphname,
    'new_data' = list()
  )
}
