# Script for Zero and missing replacement with a modified EM ilr-algorithm
# X: compositional matrix
# B1: Robust treatment (default = FALSE) (parameter rob)
# B2: Initial estimation of either the log-ratio covariance matrix (ML estimation) or unobserved data
#      complete observations (TRUE)/multiplicative simple replacement of left-censored data (FALSE)
#      (parameter ini.cov)
# P1: delta parameter for initial multiplicative simple replacement of left-censored data in proportions 
#     (default = 0.65) (parameter delta) (IF B2 = FALSE)
# Menu S1

#load("L:/CoDaCourse/CoDaCourse 17/Material/LABS 2017/Scripts 2018/CoDaLabs/Scripts CoDaPack/activzero.RData")
#X <- as.data.frame(dzeros[,2:5])
#head(X)
#B1 <- as.logical(FALSE)
#B2 <- as.logical(FALSE)
#P1 <- 0.65


library(zCompositions)
nparts <- length(X)
if (B2 == TRUE) 
{Ximp <- lrEMplus(X,dl=DL,rob=B1,ini.cov="complete.obs")
} else {Ximp <- lrEMplus(X,dl=DL,rob=B1,ini.cov="multRepl",delta=P1)}


for(k in 1:nparts)
{
  colnames(Ximp)[k] <- paste(names(X[k]),".imp",sep="")
}

# Ooutput
cdp_res = list(
  'text' = list(paste("ilr-EM MISSING/ZERO IMPUTATION")),
  'dataframe' = list(),
  'graph' = list(),
  'new_data' = data.frame(Ximp)
)

