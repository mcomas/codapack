Ximp <- zCompositions::lrEM(X,label=NA,imp.missing=TRUE,ini.cov="complete.obs",rob=V1)
names(Ximp) = paste0("z.", colnames(X))

# Ooutput
cdp_res = list(
  'text' = "",
  'dataframe' = list(),
  'graph' = list(),
  'new_data' = data.frame(Ximp)
)
