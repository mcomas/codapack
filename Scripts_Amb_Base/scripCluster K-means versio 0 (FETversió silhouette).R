# Script for compositional cluster k-means
# X: composition
# B1: fixed number of clusters (Yes or Not)
# P1: If B1 = TRUE: number of clusters. If B1 = FALSE: maximum number of clusters 
# B2: TRUE = Calinski. FALSE = Silhouette
# BaseX: Matrix containing SBP
# Menu S1

#load("L:/CoDaCourse/CoDaCourse 17/Material/LABS 2017/Scripts 2018/CoDaLabs/Scripts CoDaPack/activCluster.RData")
#X <- as.data.frame(dalim[,1:9])
#BaseX = matrix(c(1,1,1,1,1,-1,-1,-1,-1,1,1,1,-1,-1,0,0,0,0,1,1,-1,0,0,0,0,0,0,1,-1,0,0,0,0,0,0,0,0,0,0,1,-1,0,0,0,0,0,0,0,0,0,1,1,-1,-1,0,0,0,0,0,1,-1,0,0,0,0,0,0,0,0,0,1,-1), ncol = 8)



####alternative 1
#B1 <- as.logical(TRUE)
#P1 <- 3
#B2 <- as.logical(TRUE)

####alternative 2
#B1 <- as.logical(FALSE)
#P1 <- 10
#B2 <- as.logical(TRUE)

####alternative 3
#B1 <- as.logical(TRUE)
#P1 <- 3
#B2 <- as.logical(FALSE)

####alternative 4
#B1 <- as.logical(FALSE)
#P1 <- 10
#B2 <- as.logical(FALSE)

library(coda.base)

#average silhouette width or the Pearson version of Hubert's gamma

##################################

# The k-means algorithm in R: the basic function kmeans has three important
# arguments, the data set, the number of groups and a number of repetitions
# (nstart). The initial centers of groups are randomly selected. The function
# repeat nstart times the algortihm and returns the best solution in terms of
# minimum trace(W).
# We choose the best cluster according calinski index with number of clusters between 2 and 10

library(fpc)

#rm(km)
Xt <- coda.base::coordinates(X, basis = "ilr", label = "ilr.", sparse_basis = FALSE)
#Xt <- coda.base::coordinates(X, basis = sbp_basis(BaseX))
nparts=length(Xt)
for (n in 1:nparts)
{
  colnames(Xt)[n] <- paste("ilr.",n,sep="")
}
#head(Xt)

N <- nrow(Xt)
d <- dist(Xt)

# Create graphs
graphnames <- list()

if (B1 == TRUE) 
{
  k <- P1
  km <- kmeans(Xt, k, nstart = 25)
  calinski <- (km$betweenss/(k-1))/(km$tot.withinss/(N-k))
  avg.sil <- cluster.stats(d, km$cluster)$avg.silwidth
} else
{
  ks <- 2:P1      
  ASW <- sapply(ks, FUN=function(k) {
    cluster.stats(d, kmeans(Xt, centers=k, nstart=25)$cluster)$avg.silwidth
  })
  
  ksi <- which.max(ASW)
  avg.sil <- ks[ksi]
  
  
  kc <- 2:P1      
  CALI <- sapply(kc, FUN=function(k) {
    cl <- kmeans(Xt, k, nstart = 25)
    (cl$betweenss/(k-1))/(cl$tot.withinss/(N-k))
  })

  kci <- which.max(CALI)
  calinski <- kc[kci]
  
  if (B2 == TRUE) 
  {
    km <- kmeans(Xt, kci+1, nstart = 25)
  } else
  {
    km <- kmeans(Xt, ksi+1, nstart = 25)
  }

  name <- paste(tempdir(),'Average Silhouette.svg',sep="\\")
  svg(name)
  plot(ks, ASW, type="l", xlab='Number of clusters', ylab='Average Silhouette', frame=FALSE)
  dev.off()
  graphnames[1] <- name
  name <- paste(tempdir(),'Calinski index.svg',sep="\\")
  svg(name)
  plot(kc, CALI, type="l", xlab='Number of clusters', ylab='Calinski index', frame=FALSE)
  dev.off()
  graphnames[2] <- name
}

df <- cbind.data.frame(as.factor(km$cluster))
names(df) <- c("Group")

# Output
#rm(cdp_res)
cdp_res = list(
  'text' = list(paste("CLUSTER K-MEANS"),
                paste("calinski index = ",capture.output(calinski)),
                paste("Average Silhouette = ",capture.output(avg.sil)),
                paste(capture.output(km))),
  'dataframe' = list(),
  'graph' = graphnames,
  'new_data' = df
)