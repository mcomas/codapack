cdp_check = function(){
  if(!exists('X')) return("No data has been selected")
  if(!exists('Y')) return("No response has been selected")

  cond3 = NCOL(Y) == 1
  if(!cond3) return("Only one numeric response variable")
  
}
cdp_analysis = function(){
  # save.image("Rscripts/cdp_regression_real.RData")
  LM <- lm(Y~X)
  
  
  graphname <- sprintf("%s.svg", tempfile())
  svg(graphname, width = PLOT_WIDTH, height = PLOT_HEIGTH)
  oldpar <- par(oma=c(0,0,3,0), mfrow=c(2,2))
  plot(LM,sub.caption=deparse(as.formula(LM)))  # Plot the model information
  par(oldpar)
  dev.off()
  
  new_data  <-  list()
  if(V1) new_data[['residuals']] = LM$residuals
  if(V2) new_data[['fitted.values']] = LM$fitted.values
  
  if(exists('X_new')){
    new_data2 = data.frame(predict(LM, newdata = as.data.frame(X_new)))
    names(new_data2) = paste0(colnames(Y), '.pred')
  }
  # Output
  cdp_out = list(
    'text' = list(paste("LINEAR REGRESSION"),
                  paste("Dependent variable"),
                  paste(capture.output(cat(paste(colnames(Y), collapse=', ')))),
                  paste("Explanatory variables"),
                  paste(capture.output(cat(paste(colnames(X), collapse=', ')))),
                  paste(gsub("[‘’]", "'", capture.output(summary(LM))))),
    'dataframe' = list(),
    'graph' = graphname,
    'new_data' = new_data
  )
  if(exists('X_new')){
    cdp_out[['new_data2']] = new_data2
  }
  cdp_out
}

