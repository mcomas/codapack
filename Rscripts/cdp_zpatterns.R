
graphname <- sprintf("%s.svg", tempfile())

svg(graphname, width = PLOT_WIDTH, height = PLOT_HEIGTH)
sortida <- capture.output(zPat <- zCompositions::zPatterns(X,label=0,show.means=V1,bar.labels=V2))
dev.off()

new_data = list()
if(V3) new_data = data.frame('Patt.ID' = zPat)

# Output
cdp_res = list(
  'text' = sortida,
  'dataframe' = list(),
  'graph' = c(graphname),
  'new_data' = new_data
)

