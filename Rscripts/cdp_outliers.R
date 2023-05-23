# Script for compositional atypicality index
# X: compositional matrix
# P1: threshold = real
# BaseX: Matrix containing SBP
# Menu S1

#load("L:/CoDaCourse/CoDaCourse 17/Material/LABS 2017/Scripts 2018/CoDaLabs/Scripts CoDaPack/activAtyp.RData")
#X <- as.data.frame(dblood[,4:6])
#P1 <- 0.975
#BaseX = matrix(c(1, 1, -1, 1, -1, 0), ncol = 2)

# library(lattice)
# library(coda.base)
#library(graphics)
save.image('Rscripts/cdp_outliers.RData')
threshold <- V1
H <- coda.base::coordinates(X)


md <- mahalanobis(H, colMeans(H), cov(H))
chsq <- pchisq(md,df=ncol(H))
atyp <- as.integer(chsq > threshold)
c.atyp <- factor(atyp, labels = c("Non atypical",  "Atypical"))

df = data.frame("Chisq" = chsq, "Atyp" = c.atyp)

output = "No potential outlier"
if(sum(atyp) > 0){
  output = sprintf("Potential outliers: %s\n", paste(which(atyp > 0), collapse = ', '))
}

cdp_res = list(
  'text' = output,
  'dataframe' = list(),
  'graph' = list(),
  'new_data' = df
)


