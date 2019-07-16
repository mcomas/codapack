################################
# Script for compositional manova
# X: Compositional Matrix
# Y: categorical variable
# Menu S2 o S3



#load("L:/CoDaCourse/CoDaCourse 17/Material/LABS 2017/Scripts 2018/CoDaLabs/Scripts CoDaPack/activManova.RData") #name = nom de l'arxiu de dades

#####X<-as.data.frame(waste[,c("x1","x2","x3","x4","x5")])
#####Y <-G3$cluster

#X <- as.data.frame(dpolen[,1:3])
#Y <- as.data.frame(dpolen[,4])
if (is.factor(Y) == FALSE) {
         Y <- as.factor(as.matrix(Y))
     }
  
int = as.integer(Y)
nCat = nlevels(as.factor(Y)) 

Num <- matrix(nrow = nCat, ncol = ncol(X))
Den <- matrix(nrow = nCat, ncol = ncol(X))
colnames(Num)<-colnames(X)
colnames(Den)<-colnames(X)
rownames(Num)<-levels(Y)
rownames(Den)<-levels(Y)
for(i in 1:nCat) {
  Num[i,]<-exp(colMeans(log(X[int==i,])))/sum(exp(colMeans(log(X[int==i,]))))
  Den[i,]<-exp(colMeans(log(X)))/sum(exp(colMeans(log(X))))
}


graphnames <- list()
name <- paste(tempdir(),"Geometric mean barplot.svg", sep="\\")
svg(name)
par(mai=c(0.5,0.5,1,1))
barplot(log(Num/Den), beside=TRUE,legend.text=TRUE, main="Geometric mean barplot", args.legend = list(x = "topright", inset=-0.1)) 
dev.off()
graphnames[1] <- name

cdp_res = list(
  'text' = "GEOMETRIC MEAN BARPLOT", 
  'dataframe' = list(),
  'graph' = graphnames,
  'new_data' = list()
)

