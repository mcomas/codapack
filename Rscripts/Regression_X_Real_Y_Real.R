# Script for standard linear model Menu S3.
# where Y is a real dependent variable or log-ratio entered in "selected data 2" 
# and X is a set of real explanatory variables or log-ratios entered in "selected data 1"
# Select B1: stores residuals
# Select B2: stores fitted values

################# FUNCTIONS #################

generateFileName <- function(candidateName){
  name = candidateName
  nameFile = paste(name, ".svg", sep = "")

  while(file.exists(nameFile)){
    name = paste(name, "c", sep = "")
    nameFile = paste(name, ".svg", sep = "")
  }

  return(nameFile)
}

################# MAIN #################

LM <- lm(as.matrix(Y)~as.matrix(X))



# Create graphs

graphnames <- list()
name <- generateFileName(paste(tempdir(),'Plots_of_residuals',sep="\\"))
svg(name)
oldpar <- par(oma=c(0,0,3,0), mfrow=c(2,2))
plot(LM)  # Plot the model information
par(oldpar)
dev.off()
graphnames[1] <- name

#rm(DF)
DF  <-  data.frame()
if (B1 == TRUE) {
    DF <- as.data.frame(LM$residuals)
    names(DF) <-("residuals")
}
if (B2 == TRUE) {
  if(plyr::empty(DF)){
    DF <- as.data.frame(LM$fitted.values)
    names(DF) <-("fitted.values")
  } else{
    DF <- cbind.data.frame(DF, LM$fitted.values)
    names(DF) <-(c("residuals","fitted.values"))
  }
}


# Output
cdp_res = list(
  'text' = list(paste("LINEAR REGRESSION"),
                paste("Dependent variable"),
                paste(capture.output(names(Y))),
                paste("Explanatory variables"),
                paste(capture.output(names(X))),
                paste(capture.output(summary(LM)))),
  'dataframe' = list(),
  'graph' = graphnames,
  'new_data' = DF
)


