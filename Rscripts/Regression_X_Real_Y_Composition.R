# Script for compositionsl linear models where X is a real variable 
# and Y is a composition
# B1: fitted values
# B2: residuals
# BaseY: Matrix containing SBP
# Menu S3

################# LIBRARIES #################

#library(ggplot2)
library(coda.base)
#library(plyr)

#load("L:/CoDaCourse/CoDaCourse 17/Material/LABS 2017/Scripts 2018/CoDaLabs/Scripts CoDaPack/activ_arctic.RData")
#Y <- as.data.frame(darctic[,2:4])
#head(Y)
#X <- as.data.frame(darctic[,5])
#head(X)
#B1 <- as.logical(TRUE)
#B2 <- as.logical(TRUE)
#BaseY = matrix(c(1, 1, -1, 1, -1, 0), ncol = 2)

#Yt <- coda.base::coordinates(Y, basis = "ilr", label = "ilr.", sparse_basis = FALSE)

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

Yt <- coda.base::coordinates(Y, basis = coda.base::sbp_basis(BaseY))
nparts=NCOL(Yt)
colnames(Yt) = paste0('ilr.', 1:nparts)
save.image('image.RData')
# Linear model
LM <- lm(as.matrix(Yt)~., data=as.data.frame(X), y = TRUE, x = TRUE)

#summary(LM)
#put names to residuals and fitted values columns
if (B1 == TRUE) {
  for (n in 1:nparts)
  {
    colnames(LM$residuals)[n] <- paste(names(Yt[n]),".r",sep="")
  }
}

if (B2 == TRUE) {
  for (n in 1:nparts)
  {
    colnames(LM$fitted.values)[n] <- paste(names(Yt[n]),".f",sep="")
  }
}

# Calculate SSR:
FitCen <- scale(LM$fitted.values,scale=FALSE)
#sum(FitCen^2)
# Calculate SST:
#YCen <- scale(Dep,scale=FALSE)
YCen <- scale(LM$y,scale=FALSE)
#sum(YCen^2)
# Calculate R2
r2 <- (sum(FitCen^2)/sum(YCen^2))*100

# Create graphs
graphnames <- list()
for (n in 1:nparts)
{
  name <- generateFileName(paste(tempdir(),paste("Plots_of_residuals_",names(Yt[n]),sep=""),sep="\\"))
  svg(name)
  LM.temp <- lm(as.matrix(Yt[n])~as.matrix(X))
  oldpar <- par(oma=c(0,0,3,0), mfrow=c(2,2))
  title <- paste(names(Yt[n])," ~ ",names(X))
  plot(LM.temp,sub.caption=title)  # Plot the model information
  par(oldpar)
  dev.off()
  graphnames[n] <- name
}

#rm(DF)
DF <- data.frame()
#head(DF)
if (B1 == TRUE) {
  if(plyr::empty(DF)){
    DF <- as.data.frame(LM$residuals)
  } else{
    DF <- cbind.data.frame(DF, LM$residuals)
  }
}
if (B2 == TRUE) {
  if(plyr::empty(DF)){
    DF <- as.data.frame(LM$fitted.values)
  } else{
    DF <- cbind.data.frame(DF, LM$fitted.values)
  }
}

#PART NOVA. ARA TE EN COMPTE ELS NOMS DE TOTES LES X I TRANSPOSA COEFICIENTS PER PODER-LOS INVERTIR
NDF<-as.data.frame(as.factor(c("intercept",names(X))))
names(NDF)<-paste("Coefficients")
NDF=cbind.data.frame(NDF,LM$coefficients)
#FI PART NOVA.


# Ooutput
cdp_res = list(
  'text' = list(gsub("[‘’]", "'", capture.output(summary(LM))),
                capture.output(cat(sprintf("Overall R² = %0.4f", r2)))),
#  'dataframe' = list('coefficients' = LM$coefficients),
  'dataframe' = list('coefficients' = NDF),
  'graph' = graphnames,
  'new_data' = data.frame(DF)
)

