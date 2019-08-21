################################
# Script for Multiple Boxplot including Groups
# X: Matrix
# Y: categorical variable
# Menu S3

#load("L:/CoDaCourse/CoDaCourse 17/Material/LABS 2017/Scripts 2018/CoDaLabs/Scripts CoDaPack/activManova.RData") #name = nom de l'arxiu de dades
#X <- as.data.frame(dpolen[,1:3])
#Y <- as.data.frame(dpolen[,4])

Y <- as.factor(as.matrix(Y))
nparts=length(X)
nCat = nlevels(as.factor(Y)) 

mn <- min(X)
mx <- max(X)

# Create graphs
graphnames <- list()
name <- paste(tempdir(),'Boxplot.png',sep="\\")
png(name)
par(mfrow=c(1,nparts))
  for (i in 1:nparts)
  {
    means <- lm(log(X[,i])~-1+Y)$coef
    boxplot(X[,i]~Y,xlab=colnames(X)[i],ylim = c(mn, mx))
    points(1:nCat, exp(means), col = "red")
  }
dev.off()
graphnames[1] <- name

# Output
cdp_res = list(
  'text' = list(),
  'dataframe' = list(),
  'graph' = graphnames,
  'new_data' = list()
)


