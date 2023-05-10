# Script for compositional atypicality index
# X: compositional matrix
# P1: threshold = real
# BaseX: Matrix containing SBP
# Menu S1

#load("L:/CoDaCourse/CoDaCourse 17/Material/LABS 2017/Scripts 2018/CoDaLabs/Scripts CoDaPack/activAtyp.RData")
#X <- as.data.frame(dblood[,4:6])
#P1 <- 0.975
#BaseX = matrix(c(1, 1, -1, 1, -1, 0), ncol = 2)

library(lattice)
library(coda.base)
#library(graphics)

threshold <- P1
Xt <- coda.base::coordinates(X)
#Xt <- coda.base::coordinates(X, basis = coda.base::sbp_basis(BaseX))
nparts=length(Xt)
for (n in 1:nparts)
{
  colnames(Xt)[n] <- paste("ilr.",n,sep="")
}
#head(Xt)
nr <- nrow(X)
md <- mahalanobis(Xt, colMeans(Xt), cov(Xt))
chsq <- pchisq(md,df=ncol(Xt))
atyp <- as.integer(cut(chsq,breaks=c(0,threshold,1)))-1
c.atyp <- NA
c.atyp[atyp==0] <- "Non atypical"
c.atyp[atyp==1] <- "Atypical"
df <- cbind.data.frame(chsq,c.atyp)
#df <- cbind.data.frame(chsq,as.factor(atyp))
names(df) <- c("Chisq","Atyp")
index <- c(1:nr)
#rm(sortida)
sortida <- list()
sortida[1] <- paste("POTENTIAL OUTLIERS")
sortida[2] <- paste("atypical observations ",capture.output(which(atyp %in% 1)))

#codi de debugg
#
#
#sink(file='out.txt')
#str(chsq)
#str(index)
#str(c.atyp)
#sink()
#rm(graphnames)
#graphnames <- list()
#name <- paste("Atipical_observations.png",sep="")
#codi de debugg
#sink('out.txt')
#outputs = dev.list()
#print(outputs)
#sink()
#png(name)
#panel = function (x, y, ...) {
#  lattice::panel.xyplot(x, y, ...)
#  v <- (y > threshold)
#  lattice::panel.text(x[v], y[v], labels=index[v], pos=3)}

#lattice::xyplot(chsq~index, groups = c.atyp, auto.key = TRUE, xlab = 'Observation', ylab = 'Chi square', 
#       par.settings = list(superpose.symbol = list(pch = 19, cex = 1.5, col = c("orange", "blue"))),panel=panel)
#dev.off()
#graphnames[1] <- name


# Output
#rm(cdp_res)
cdp_res = list(
  'text' = sortida,
  'dataframe' = list(),
  'graph' = list(),
  'new_data' = df
)


