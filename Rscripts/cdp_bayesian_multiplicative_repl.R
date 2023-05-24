Ximp = zCompositions::cmultRepl(X,label=0, method=V1, threshold=0.5, output="p-counts")
names(Ximp) = paste0("z.", colnames(X))

# Ooutput
cdp_res = list(
  'text' = "",
  'dataframe' = list(),
  'graph' = list(),
  'new_data' = data.frame(Ximp)
)
