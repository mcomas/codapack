alr = function(X){
    logX <- log(X)
    logX[,-NCOL(X)] - logX[,NCOL(X)]
}
alrInv = function(X){
    cX = cbind(exp(X),1)
    cX / rowSums(cX)
}
