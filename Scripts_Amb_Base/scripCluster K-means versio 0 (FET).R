# Script for compositional cluster k-means
# X: composition
# B1: fixed number of clusters (Yes or Not)
# P1: (If B1 = TRUE) number of clusters 
# P2: (If B1 = FALSE) maximum number of clusters 
# BaseX: Matrix containing SBP
# Menu S1

#load("L:/CoDaCourse/CoDaCourse 17/Material/LABS 2017/Scripts 2018/CoDaLabs/Scripts CoDaPack/activCluster.RData")
#X <- as.data.frame(dalim[,1:9])
#BaseX = matrix(c(1,1,1,1,1,-1,-1,-1,-1,1,1,1,-1,-1,0,0,0,0,1,1,-1,0,0,0,0,0,0,1,-1,0,0,0,0,0,0,0,0,0,0,1,-1,0,0,0,0,0,0,0,0,0,1,1,-1,-1,0,0,0,0,0,1,-1,0,0,0,0,0,0,0,0,0,1,-1), ncol = 8)
                     
                     

####alternative 1
#B1 <- as.logical(TRUE)
#P1 <- 3

####alternative 2
#B1 <- as.logical(FALSE)
#P1 <- 10

#library(coda.base)

#average silhouette width or the Pearson version of Hubert's gamma

##################################

# The k-means algorithm in R: the basic function kmeans has three important
# arguments, the data set, the number of groups and a number of repetitions
# (nstart). The initial centers of groups are randomly selected. The function
# repeat nstart times the algortihm and returns the best solution in terms of
# minimum trace(W).
# We choose the best cluster according calinski index with number of clusters between 2 and 10

#rm(km)

Xt <- coda.base::coordinates(X, basis = "ilr", label = "ilr.", sparse_basis = FALSE)
#Xt <- coda.base::coordinates(X, basis = coda.base::sbp_basis(BaseX))
nparts=length(Xt)
for (n in 1:nparts)
{
  colnames(Xt)[n] <- paste("ilr.",n,sep="")
}
#head(Xt)

N <- nrow(Xt)
if (B1 == TRUE) 
  {
    k <- P1
    set.seed(123456789)
    km <- kmeans(Xt, k, nstart = 25)
    calinski <- (km$betweenss/(k-1))/(km$tot.withinss/(N-k))
  } else
    {
      calinski <- 0
      ngroups <- 2
      for(k in 2:P1)
      {
        set.seed(123456789)
        cl <- kmeans(Xt, k, nstart = 25)
        cnt <- (cl$betweenss/(k-1))/(cl$tot.withinss/(N-k))
        if (cnt > calinski) 
        {
          calinski <- cnt
          ngroups <- k
          km <- cl
        }
      }
}

df <- cbind.data.frame(as.factor(km$cluster))
names(df) <- c("Group")

# Output
#rm(cdp_res)
cdp_res = list(
  'text' = list(paste("CLUSTER K-MEANS"),
                paste("calinski index = ",capture.output(calinski)),
                paste(capture.output(km))),
  'dataframe' = list(),
  'graph' = list(),
  'new_data' = df
)



