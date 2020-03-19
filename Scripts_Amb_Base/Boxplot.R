################################
# Script for Multiple Boxplot including Groups
# X: Matrix
# Y: categorical variable
# Menu S3

#load("L:/CoDaCourse/CoDaCourse 17/Material/LABS 2017/Scripts 2018/CoDaLabs/Scripts CoDaPack/activManova.RData") #name = nom de l'arxiu de dades
#X <- as.data.frame(dpolen[,1:3])
#Y <- as.data.frame(dpolen[,4])
#load("L:/CoDaCourse/Alimentation.RData") #name = nom de l'arxiu de dades
#X <- as.data.frame(Alimentation[,1:9])
#Y <- as.data.frame(Alimentation[,12])

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

if (is.null(Y)) { # sense grups
  # Create graphs
  graphnames <- list()
  name <- generateFileName(paste(tempdir(),'Boxplot',sep="\\"))
  svg(name)
  nr <- (nparts %/% 5) + 1
  if(nparts %% nr == 0){nc = nparts %/% nr}else{nc = (nparts %/% nr)+1}
  par(mfrow=c(nr,nc))
  #par(mfrow=c(1,nparts))
  for (i in 1:nparts)
  {
    means <- lm(log(X[,i])~1)$coef
    boxplot(X[,i],xlab=colnames(X)[i],ylim = c(mn, mx))
    points(1,exp(means), col = "red")
    #points(1:nCat, exp(means), col = "red")
  }
  par(mfrow=c(1,1))
  dev.off()
  graphnames[1] <- name
} else { # amb grups
  Y <- as.factor(as.matrix(Y))
  nCat = nlevels(as.factor(Y)) 

# Create graphs
  graphnames <- list()
  name <- generateFileName(paste(tempdir(),'Boxplot',sep="\\"))
  svg(name)
  nr <- (nparts %/% 5) + 1
  if(nparts %% nr == 0){nc = nparts %/% nr}else{nc = (nparts %/% nr)+1}
  par(mfrow=c(nr,nc))
  #par(mfrow=c(1,nparts))
  for (i in 1:nparts)
  {
    means <- lm(log(X[,i])~-1+Y)$coef
    boxplot(X[,i]~Y,xlab=colnames(X)[i],ylim = c(mn, mx))
    points(1:nCat, exp(means), col = "red")
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