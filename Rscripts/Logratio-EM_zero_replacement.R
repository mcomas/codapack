# Script for Zero replacement with a modified EM ilr-algorithm
# X: compositional matrix
# B1: Robust treatment (default = FALSE) (parameter rob)
# P1: label for unobserved values (default = 0) (parameter label)
# B2: Initial estimation of either the log-ratio covariance matrix (ML estimation) or unobserved data
#      complete observations (TRUE)/multiplicative simple replacement of left-censored data (FALSE)
#      (parameter ini.cov)
# P2: delta parameter for initial multiplicative simple replacement of left-censored data in proportions 
#     (default = 0.65) (parameter delta) (IF B2 = FALSE)
# B3: unobserved are treated as missing (TRUE) instead of left-censored (FALSE) 
#     (default = FALSE) (parameter imp.missing)
# P3: Numeric vector of detection limits/thresholds of each part (default = NULL) (parameter dl)
# Menu S1

#load("L:/CoDaCourse/CoDaCourse 17/Material/LABS 2017/Scripts 2018/CoDaLabs/Scripts CoDaPack/activzero.RData")
#X <- as.data.frame(dzeros[,2:5])
#load("L:/CoDaCourse/CoDaCourse 17/Material/LABS 2017/Scripts 2018/CoDaLabs/Scripts CoDaPack/fglass.rda")
#X <- as.data.frame(glass[,1:9])
#head(X)
#B1 <- as.logical(FALSE)
#P1 <- 0
#B2 <- as.logical(FALSE)
#P2 <- 0.65
#B3 <- as.logical(FALSE)
#P3 <- rep(0.01,9)


#library(zCompositions)
nparts <- length(X)
if (B2 == TRUE) 
{Ximp <- zCompositions::lrEM(X,label=P1,dl=P3,rob=B1,ini.cov="complete.obs",imp.missing=B3)
} else {Ximp <- zCompositions::lrEM(X,label=P1,dl=P3,rob=B1,ini.cov="multRepl",delta=P2,imp.missing=B3)}


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


