Ximp <- zCompositions::lrEMplus(X,dl=DL,rob=V3,ini.cov=V1,frac=V2)
names(Ximp) = paste0("z.", colnames(X))

# Ooutput
cdp_res = list(
  'text' = "",
  'dataframe' = list(),
  'graph' = list(),
  'new_data' = data.frame(Ximp)
)
