save.image("Rscripts/cdp_nonparametric_zero_repl.RData")

Ximp <- zCompositions::multRepl(X,label=0,dl=DL,frac=V1)
names(Ximp) = paste0("z.", colnames(X))

# Ooutput
cdp_res = list(
  'text' = "",
  'dataframe' = list(),
  'graph' = list(),
  'new_data' = data.frame(Ximp)
)