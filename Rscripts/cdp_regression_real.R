
LM <- lm(Y~X)



# Create graphs

# graphnames <- list()
# name <- generateFileName(paste(tempdir(),'Plots_of_residuals',sep="\\"))
# svg(name)
# oldpar <- par(oma=c(0,0,3,0), mfrow=c(2,2))
# plot(LM)  # Plot the model information
# par(oldpar)
# dev.off()
# graphnames[1] <- name

new_data  <-  list()
if(V1) new_data[['residuals']] = LM$residuals
if(V2) new_data[['fitted.values']] = LM$fitted.values


# Output
cdp_res = list(
  'text' = list(paste("LINEAR REGRESSION"),
                paste("Dependent variable"),
                paste(capture.output(cat(paste(colnames(Y), collapse=', ')))),
                paste("Explanatory variables"),
                paste(capture.output(cat(paste(colnames(X), collapse=', ')))),
                paste(gsub("[‘’]", "'", capture.output(summary(LM))))),
  'dataframe' = list(),
  'graph' = c(), #graphnames,
  'new_data' = new_data
)


