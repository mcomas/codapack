save.image("Rscripts/cdp_regression_coda_explanatory.RData")
           
H = coda.base::coordinates(X, basis = coda.base::sbp_basis(BasisX))
colnames(H) = paste0('ilr.', 1:ncol(H))

str_y = colnames(Y)
if(ncol(Y) > 1) str_y = sprintf("cbind(%s)", paste(colnames(Y), collapse=','))
str_x = paste(colnames(H), collapse='+')
str_frm = sprintf("%s ~ %s", str_y, str_x)

. = as.data.frame(cbind(Y,H))
LM = eval(parse(text = sprintf("lm(%s, .)", str_frm)))

#summary(LM)


graphname <- sprintf("%s.svg", tempfile())
svg(graphname, width = PLOT_WIDTH, height = PLOT_HEIGTH)
oldpar <- par(oma=c(0,0,3,0), mfrow=c(2,2))
plot(LM,sub.caption=deparse(as.formula(LM)))  # Plot the model information
par(oldpar)
dev.off()


new_data  <-  list()
if(V1) new_data[['residuals']] = LM$residuals
if(V2) new_data[['fitted.values']] = LM$fitted.values

text_output = capture.output(summary(LM))
text_output = gsub("[‘’]", "'", text_output)

# Ooutput
cdp_res = list(
  'text' = list(text_output),
  'dataframe' = list(), #list('coefficients' = LM$coefficients),
  'graph' = graphname,
  'new_data' = new_data
)