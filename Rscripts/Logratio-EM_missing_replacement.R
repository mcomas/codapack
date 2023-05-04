# Script for Missing replacement with a modified EM ilr-algorithm
# X: compositional matrix
# B1: Robust treatment (default = FALSE) (parameter rob)
# P1: label for unobserved values (default = NA)  (parameter label)
# Menu S1

#load("L:/CoDaCourse/CoDaCourse 17/Material/LABS 2017/Scripts 2018/CoDaLabs/Scripts CoDaPack/activmiss.RData")
#X <- as.data.frame(dmiss[,1:5])
#B1 <- as.logical(TRUE)
#P1 <- NaN

#library(zCompositions)
#rm(Ximp)
nparts <- length(X)
Ximp <- zCompositions::lrEM(X,label=P1,imp.missing=TRUE,ini.cov="complete.obs",rob=B1)

for(k in 1:nparts)
{
  colnames(Ximp)[k] <- paste(names(X[k]),".imp",sep="")
}

# Ooutput
cdp_res = list(
  'text' = list(paste("ilr-EM IMPUTATION")),
  'dataframe' = list(),
  'graph' = list(),
  'new_data' = data.frame(Ximp)
)


