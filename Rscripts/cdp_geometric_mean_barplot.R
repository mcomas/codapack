Y <- as.factor(GROUP)
X = as.data.frame(X)  
nOrig <- length(X)
nobs <- nrow(X)
X=X/rowSums(X)
#rm(sortida)
# centers of each group ci and whole center 
    
int = as.integer(Y)
Mat = cbind.data.frame(X,int)
nCat = nlevels(as.factor(Y)) 
  
#nRow <- choose(nCat,2)
nRow <- nOrig
nCol <- nCat
rm(cmat)
#cmat <- matrix(, nrow = choose(nCat,2), ncol = nCat)
cmat <- matrix(0, nrow = nCat, ncol = nOrig)

cmat[1,]=exp(apply(log(Mat[Mat[,nOrig+1]==1,1:nOrig]),2,mean))
Num <- rbind(cmat[1,])
cT <- exp(apply(log(Mat[,1:nOrig]),2,mean))
Den <- rbind(cT)
for(i in 2:nCat) {
   cmat[i,]=exp(apply(log(Mat[Mat[,nOrig+1]==i,1:nOrig]),2,mean))
   Num <- rbind(Num,cmat[i,])
   Den <- rbind(Den,cT)
 }

graphname = sprintf("%s.pdf", tempfile())
svg(graphname, width = PLOT_WIDTH, height = PLOT_HEIGTH)
barplot(log(Num/Den), beside=TRUE,col=c(1:nCat))
dev.off()

cdp_res = list(
  'text' = "",
  'dataframe' = list(),
  'graph' = graphname,
  'new_data' = list()
)