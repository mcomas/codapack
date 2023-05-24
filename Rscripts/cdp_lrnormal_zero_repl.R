Ximp <- zCompositions::lrEM(X,label=0,dl=DL,frac=V2,rob=V3,ini.cov=V1)
names(Ximp) = paste0("z.", colnames(X))

# Ooutput
cdp_res = list(
  'text' = "",
  'dataframe' = list(),
  'graph' = list(),
  'new_data' = data.frame(Ximp)
)
