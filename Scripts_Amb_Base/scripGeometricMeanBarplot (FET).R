################################
# Script for compositional manova
# X: Compositional Matrix
# Y: categorical variable
# Menu S2 o S3



#load("L:/CoDaCourse/CoDaCourse 17/Material/LABS 2017/Scripts 2018/CoDaLabs/Scripts CoDaPack/activManova.RData") #name = nom de l'arxiu de dades

#X <- as.data.frame(dpolen[,1:3])
#Y <- as.data.frame(dpolen[,4])
if (is.factor(Y) == FALSE) {
         Y <- as.factor(as.matrix(Y))
     }
  
nOrig <- length(X)
nobs <- nrow(X)
#rm(sortida)
# centers of each group ci and whole center 
    
int = as.integer(Y)
Mat = cbind.data.frame(X,int)
nCat = nlevels(as.factor(Y)) 
  
nRow <- choose(nCat,2)
nCol <- nCat
rm(cmat)
cmat <- matrix(, nrow = choose(nCat,2), ncol = nCat)
  
cmat[1,]=exp(apply(log(Mat[Mat[,nOrig+1]==1,1:nOrig]),2,mean))
Num <- rbind(cmat[1,])
cT <- exp(apply(log(Mat[,1:nOrig]),2,mean))
Den <- rbind(cT)
for(i in 2:nCat) {
   cmat[i,]=exp(apply(log(Mat[Mat[,nOrig+1]==i,1:nOrig]),2,mean))
   Num <- rbind(Num,cmat[i,])
   Den <- rbind(Den,cT)
 }

graphnames <- list()
#name <- paste("Bar_plot_comparing_all_the_centers_with_the_whole_center.png",sep="")
name <- "Geometric mean barplot.png"
png(name)
barplot(log(Num/Den), beside=TRUE,col=c(1:nCat))
dev.off()
graphnames[1] <- name

cdp_res = list(
  'text' = "GEOMETRIC MEAN BARPLOT",
  'dataframe' = list(),
  'graph' = graphnames,
  'new_data' = list()
)
