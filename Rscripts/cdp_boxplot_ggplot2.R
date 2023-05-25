# load('Rscripts/cdp_boxplot.RData')

################# MAIN #################
library(data.table)
library(ggplot2)

dat = as.data.table(X)
vnum = names(dat)
if(exists("GROUP")){
  dat$group = GROUP
}else{
  dat$group = 0
}
dplot = melt(dat, measure = vnum)
dplot_stats = dplot[, .(gmean = coda.base::gmean(value),
                        amean = mean(value)), .(group, variable)]
  
p = NULL
if(exists("GROUP")){
  p = ggplot(data = dplot, aes(x = group, y = value)) +
    geom_boxplot(linetype = 'dashed', outlier.shape = 1) +
    stat_boxplot(aes(ymin = after_stat(lower), ymax = after_stat(upper))) +
    stat_boxplot(geom = "errorbar", aes(ymin = after_stat(ymax))) +
    stat_boxplot(geom = "errorbar", aes(ymax = after_stat(ymin))) +
    facet_wrap(~variable, nrow = (ncol(X) %/% 5) + 1, scales = 'free_x') +
    theme_bw() + labs(x = "", y = "", col = '')
  if(V1){
    p = p +
      geom_point(data = dplot_stats, aes(x = group, y = gmean, col = 'Geometric mean')) + theme(legend.position = 'top')
  }
  if(V2){
    p = p +
      geom_point(data = dplot_stats, aes(x = group, y = amean, col = 'Arithmetic mean')) + theme(legend.position = 'top')
  }
}else{
  p = ggplot(data = dplot, aes(x = variable, y = value)) +
    geom_boxplot(linetype = 'dashed', outlier.shape = 1) +
    stat_boxplot(aes(ymin = after_stat(lower), ymax = after_stat(upper))) +
    stat_boxplot(geom = "errorbar", aes(ymin = after_stat(ymax))) +
    stat_boxplot(geom = "errorbar", aes(ymax = after_stat(ymin))) +
    theme_minimal() + labs(x = "", y = "", col = "")
  if(V1){
    p = p +
      geom_point(data = dplot_stats, aes(x = variable, y = gmean, col = 'Geometric mean'), shape = 15) + theme(legend.position = 'top')
  }
  if(V2){
    p = p +
      geom_point(data = dplot_stats, aes(x = variable, y = amean, col = 'Arithmetic mean'), shape = 15) + theme(legend.position = 'top')
  }
}
graphname = c()
graphname = sprintf("%s.pdf", tempfile())
svg(filename = graphname, width = PLOT_WIDTH, height = PLOT_HEIGTH)
plot(p)
dev.off()

# ggsave(graphname, plot = p, width = PLOT_WIDTH, height = PLOT_HEIGTH)
save.image('Rscripts/cdp_boxplot.RData')
# Output
cdp_res = list(
  'text' = "",
  'dataframe' = list(),
  'graph' = graphname,
  'new_data' = list()
)