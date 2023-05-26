

text = ""
if(V1 == 'Shapiro-Wilk'){
  text = "<b>Shapiro-Wilk Normality Test</b>\n\n"
  dres = t(sapply(apply(X, 2, shapiro.test),
                  function(tst) c(tst$statistic, 'p-value' = tst$p.value)))
}

if(V1 == "Kolmogorov-Smirnov"){
  text = "<b>Kolmogorov-Smirnov Normality Test</b>\n\n"
  dres = t(sapply(apply(X, 2, function(x) ks.test(x, pnorm, 
                                                  mean = mean(x),
                                                  sd = sd(x))),
                  function(tst) c(tst$statistic, 'p-value' = tst$p.value)))
}

if(exists("dres")){
  d = data.frame('Variable' = rownames(dres))
  rownames(dres) = NULL
  dres = cbind(d, dres)
  text = c(text, capture.output(knitr::kable(dres, format = 'html')))
}

# Output
cdp_res = list(
  'text' = text,
  'dataframe' = list(),
  'graph' = list(),
  'new_data' = list()
)

