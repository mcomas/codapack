################################
# Script for Multiple Boxplot including Groups
# X: Matrix
# Y: categorical variable
# P1: print mean (none, geometric, arithmetic). Deffault = geometric
# Menu S3

#load("L:/AConfinament/LABS_DATA_FILES/activCluster.RData")
#X <- as.data.frame(dpolen[,1:3])
#Y <- as.data.frame(dpolen[,4])
#load("L:/AConfinament/LABS_DATA_FILES/Alimentation.RData") #name = nom de l'arxiu de dades
#X <- as.data.frame(Alimentation[,1:9])
#Y <- as.data.frame(Alimentation[,12])

#P1 <- "geometric"

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

nparts=length(X)
mn <- min(X)
mx <- max(X)

if ((mn <= 0) && (P1 == "geometric"))
{
  # Output
  cdp_res = list(
    'text' = list(paste("Some data is negative or zero. Geometrical mean can not be calculated")),
    'dataframe' = list(),
    'graph' = list(),
    'new_data' = list()
  )
}else{
if (is.null(Y)) { # without grups
  # Create graphs
  graphnames <- list()
  name <- generateFileName(paste(tempdir(),'Boxplot',sep="\\"))
  svg(name)
  nr <- (nparts %/% 5) + 1
  if(nparts %% nr == 0){nc = nparts %/% nr}else{nc = (nparts %/% nr)+1}
  par(mfrow=c(nr,nc))
  for (i in 1:nparts)
  {
    boxplot(X[,i],xlab=colnames(X)[i],ylim = c(mn, mx))
    if (P1 == "geometric")
    {
      means <- lm(log(X[,i])~1)$coef
      points(1,exp(means), col = "red", pch = 19)
    }else{
      if (P1 == "arithmetic")
      {
        means <- mean(X[,i])
        points(1,means, col = "red", pch = 19)
      }
    }
  }
  par(mfrow=c(1,1))
  dev.off()
  graphnames[1] <- name
} else { # with grups
  Y <- as.factor(as.matrix(Y))
  nCat = nlevels(as.factor(Y)) 
  
  # Create graphs
  graphnames <- list()
  name <- generateFileName(paste(tempdir(),'Boxplot',sep="\\"))
  svg(name)
  nr <- (nparts %/% 5) + 1
  if(nparts %% nr == 0){nc = nparts %/% nr}else{nc = (nparts %/% nr)+1}
  par(mfrow=c(nr,nc))
  for (i in 1:nparts)
  {
    boxplot(X[,i]~Y,xlab=colnames(X)[i],ylim = c(mn, mx))
    if (P1 == "geometric")
    {
      means <- lm(log(X[,i])~-1+Y)$coef
      points(1:nCat, exp(means), col = "red", pch = 19)
    }else{
      if (P1 == "arithmetic")
      {
        means <- aggregate(X[,i], by=list(Y), mean)
        points(1:nCat,means$x, col = "red", pch = 19)
      }
    }
  }
  par(mfrow=c(1,1))
  dev.off()
  graphnames[1] <- name
}

# Output
cdp_res = list(
  'text' = list(),
  'dataframe' = list(),
  'graph' = graphnames,
  'new_data' = list()
)
}