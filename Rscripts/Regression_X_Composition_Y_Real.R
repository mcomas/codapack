# Script for compositionsl linear model where Y is a real variable 
# and X is a composition
# B1: fitted values
# B2: residuals
# BaseX: Matrix containing SBP
# Menu S3

################# LIBRARIES #################

#library(ggplot2)
library(coda.base)
#library(plyr)

#load("L:/CoDaCourse/CoDaCourse 17/Material/LABS 2017/Scripts 2018/CoDaLabs/Scripts CoDaPack/activ_arctic.RData")
#Y <- as.data.frame(darctic[,5])
#head(Y)
#X <- as.data.frame(darctic[,2:4])
#head(X)
#B1 <- as.logical(TRUE)
#B2 <- as.logical(TRUE)
#BaseX = matrix(c(1, 1, -1, 1, -1, 0), ncol = 2)

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

Xt <- coda.base::coordinates(X, basis = coda.base::sbp_basis(BaseX))
nparts=NCOL(Xt)
colnames(Xt) = paste0('ilr.', 1:nparts)

df <- cbind.data.frame(Xt,Y)

# Linear model
#rm(formul)
#formul <- paste(colnames(df[nparts+1]),"~",colnames(df[1]),"+",sep="")
#for(n in 2:nparts) {
#  formul <- paste(formul,colnames(df[n]), collapse= "+",sep="")
formul <- paste(colnames(df[nparts+1]),"~",colnames(df[1]),sep="")
if (nparts > 1)
{
  for(n in 2:nparts) {
    formul <- paste(formul,"+",colnames(df[n]), sep="")
  }
}
#rm(LM)
LM <- lm(as.formula(formul),data=df)

#summary(LM)

# Create graphs
# Create graphs
graphnames <- list()
name <- generateFileName(paste(tempdir(),'Plots_of_residuals',sep="\\"))
svg(name)
oldpar <- par(oma=c(0,0,3,0), mfrow=c(2,2))
plot(LM,sub.caption=formul)  # Plot the model information
par(oldpar)
dev.off()
graphnames[1] <- name

#rm(DF)
DF  <-  data.frame()
#head(DF)
#if (B1 == TRUE) {
#  if(plyr::empty(DF)){
#    DF <- as.data.frame(LM$residuals)
#  } else{
#    DF <- cbind.data.frame(DF, LM$residuals)
#  }
#}
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

text_output = gsub("as.formula(formul)", formul, capture.output(summary(LM)), fixed = TRUE)
text_output = gsub("[‘’]", "'", text_output)

# Ooutput
cdp_res = list(
  'text' = list(text_output),
  'dataframe' = list('coefficients' = LM$coefficients),
  'graph' = graphnames,
  'new_data' = DF
)


