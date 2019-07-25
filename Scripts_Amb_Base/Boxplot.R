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

# Create graphs
graphnames <- list()
name <- paste(tempdir(),'Boxplot.svg',sep="\\")
svg(name)
par(mfrow=c(1,nparts))
  for (n in 1:nparts)
  {
    means <- lm(X[,n]~-1+Y)$coef
    boxplot(X[,n]~Y,xlab=colnames(X)[n])
    points(1:nCat, means, col = "red")
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


