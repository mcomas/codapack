save.image('Rscripts/cdp_mosaic_plot.RData')

graphname = sprintf("%s.pdf", tempfile())
svg(filename = graphname, width = PLOT_WIDTH, height = PLOT_HEIGTH)

plot(as.factor(as.matrix(X)),as.factor(as.matrix(GROUP)),
     xlab=colnames(X), ylab="")
dev.off()


cdp_res = list(
  'text' = "",
  'dataframe' = list(),
  'graph' = graphname,
  'new_data' = list())
