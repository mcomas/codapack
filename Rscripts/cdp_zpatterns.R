# Script for Zero Patterns
# X: compositional matrix
# B1: Show Means (Default = FALSE)
# B2: Show Percentages (default = FALSE) 
# Menu S1

#load("L:/CoDaCourse/CoDaCourse 17/Material/LABS 2017/Scripts 2018/CoDaLabs/Scripts CoDaPack/activmiss.RData")
#X <- as.data.frame(dmiss[,1:5])
#B1 = TRUE
#B2 = TRUE
#B3 = TRUE

################# MAIN #################


graphname <- sprintf("%s.svg", tempfile())

svg(graphname, width = PLOT_WIDTH, height = PLOT_HEIGTH)
sortida <- capture.output(zPat <- zCompositions::zPatterns(X,label=0,show.means=B1,bar.labels=B2))
dev.off()

new_data = list()
if(B3) new_data = data.frame('Patt.ID' = zPat)

# Output
cdp_res = list(
  'text' = sortida,
  'dataframe' = list(),
  'graph' = c(graphname),
  'new_data' = new_data
)

