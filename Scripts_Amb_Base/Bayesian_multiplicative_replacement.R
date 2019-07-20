# Script for Bayesian-multiplicative replacement
# X: compositional matrix
# P1: Method = {"GBM","SQ","BL","CZM"}. Default = "GBM"). Parameter method
# P2: Output format: {"prop","counts"}. imputed proportions (prop (TRUE) default) or pseudo-counts (counts (FALSE)). Parameter output
# Menu S1

#load("L:/CoDaCourse/CoDaCourse 17/Material/LABS 2017/Scripts 2018/CoDaLabs/Scripts CoDaPack/activzero.RData")
#X <- as.data.frame(dzeros[,2:5])
#head(X)

# ALTERNATIVE 1: Method GBM

# ALTERNATIVE 2: Method SQ 
#P1 <- "SQ"
#B1 <- as.logical(TRUE)

# ALTERNATIVE 3: Method BL
#P1 <- "BL"
#B1 <- as.logical(TRUE)

# ALTERNATIVE 4: Method CZM
#P1 <- "CZM"
#B1 <- as.logical(TRUE)

#library(zCompositions)
Ximp <- zCompositions::cmultRepl(X,label=0,delta=0.65,method=P1,threshold=0.5,output=P2,correct=TRUE)

nparts <- length(X)
for(k in 1:nparts)
{
  colnames(Ximp)[k] <- paste(names(X[k]),".imp",sep="")
}

# Output
cdp_res = list(
  'text' = list(paste("BAYESIAN-MULTIPLICATIVE REPLACEMENT")),
  'dataframe' = list(),
  'graph' = list(),
  'new_data' = data.frame(Ximp))