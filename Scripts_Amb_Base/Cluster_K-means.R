# Script for compositional cluster k-means
# X: composition
# B1: fixed number of clusters (Yes or Not)
# P1: If B1 = TRUE: number of clusters. If B1 = FALSE: maximum number of clusters 
# B2: TRUE = Calinski. FALSE = Silhouette
# BaseX: Matrix containing SBP
# Menu S1

#load("E:/activCluster.RData")
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

################# LIBRARIES #################

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
library(cluster)

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

#rm(km)
B = ilr_basis(ncol(X))
Xt <- coda.base::coordinates(X, B)
#Xt <- coda.base::coordinates(X, basis = sbp_basis(BaseX))
nparts=NCOL(Xt)

#head(Xt)
i.nona = which(rowSums(is.na(Xt))==0)

Xt.nona = Xt[i.nona,]
# Create graphs
graphnames <- list()

if(B1){
  krange = P1
}else{
  krange = 2:P1
}
l_km = lapply(krange, function(k){
  set.seed(123456789)
  kmeans(Xt.nona, k, nstart = 25)
})


v_calinski = sapply(l_km, function(km) fpc::calinhara(Xt.nona, km$cluster))
dist.X.nona = dist(Xt.nona)
v_silhouette = sapply(l_km, function(km) mean(cluster::silhouette(km$cluster, dist.X.nona)[,'sil_width']))


if(B2){ # Calinski
  i_best = which.max(v_calinski)
}else{ # Silhoutte
  D.X.nona = 
  i_best = which.max(v_silhouette)
}

km_best = l_km[[i_best]]


if(!B1){
  name <- generateFileName(paste(tempdir(),'Average Silhouette',sep="\\"))
  svg(name)
  plot(krange, v_silhouette, type="l", xlab='Number of clusters', ylab='Average Silhouette', frame=FALSE)
  dev.off()
  graphnames[1] <- name
  name <- generateFileName(paste(tempdir(),'Calinski index',sep="\\"))
  svg(name)
  plot(krange, v_calinski, type="l", xlab='Number of clusters', ylab='Calinski index', frame=FALSE)
  dev.off()
  graphnames[2] <- name
}

gr = rep('na', nrow(X))
gr[i.nona] = km_best$cluster
df = data.frame('Group' = gr)

dres = data.frame('Centers' = krange,
                  'CH index' = v_calinski,
                  'AS index' = v_silhouette)

dcenters = as.data.frame(composition(km_best$centers, B))
names(dcenters) = colnames(X)

dcenters$Size = km_best$size
dcenters$`Within SS` = km_best$withinss



output_text = list(capture.output(dres),
                   c("Centers:", capture.output(dcenters)),
                   sprintf("(between_SS / total_SS =  %.2f %%)", 100* km_best$betweenss / km_best$totss))
if(nrow(Xt) - length(i.nona) > 0){
  output_text = c(output_text, 
                  sprintf("(%d observations deleted due to missingness)", nrow(Xt) - length(i.nona)))
}
# Output
#rm(cdp_res)
cdp_res = list(
  'text' = output_text,
  'dataframe' = list(),
  'graph' = graphnames,
  'new_data' = df
)