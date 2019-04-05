# Script for compositional linear discriminant analysis
# X: Compositional Matrix
# Y: categorical variable
# B1: use of uniform prior
# B2: predicting for a new sample Z
# Z: Compositional matrix with same parts as X <- only if B2 is true
# B3: New column: Discriminant scores
# B4: New column: Maximum a Posteriori Probability classification
# B5: New column: Posterior probabilities for the classes
# BaseX: Matrix containing SBP

# Menu S3

# execute function lda() (default: prior proportional to the group size) and

# describe the results

#load("L:/CoDaCourse/CoDaCourse 17/Material/LABS 2017/Scripts 2018/CoDaLabs/Scripts CoDaPack/activDA.RData") #name = nom de l'arxiu de dades

#rm(X)
#X <- as.data.frame(dpetra[,1:3])
#Y <- as.data.frame(dpetra[,4])
#B1 <- as.logical(TRUE)
#B2 <- as.logical(TRUE)
#P1 <- as.data.frame(t(c(0.6,0.1,0.3)))
#B3 <- as.logical(TRUE)
#B4 <- as.logical(TRUE)
#B5 <- as.logical(TRUE)
#BaseX = matrix(c(1, 1, -1, 1, -1, 0), ncol = 2)
#Z <- X


#install.packages("zCompositions")
library(zCompositions)
library(coda.base)
library(plyr)


#Xt <- coda.base::coordinates(X, basis = "ilr", label = "ilr.", sparse_basis = FALSE)
Xt <- coda.base::coordinates(X, basis = coda.base::sbp_basis(BaseX))
nparts=length(Xt)
for (n in 1:nparts)
{
  colnames(Xt)[n] <- paste("ilr.",n,sep="")
}
#head(Xt)

lda1 <- MASS::lda(Xt,t(Y))

#rm(sortida)
sortida <- list(paste("DISCRIMINANT ANALYSIS"))
sortida <- list(sortida,paste(capture.output(lda1)))


################################

# visualizing the results: panels of histograms and overlayed density plots

# for 1st discriminant function

#rm(graphnames)
graphnames <- list()
name <- paste(tempdir(),'panels_of_histograms_and_overlayed_density_plots.png',sep="\\")
png(name)
plot(lda1, dimen=1, type="both")
dev.off()
graphnames[1] <- name

################################

# assess the accuracy of the prediction: percent correct for each group

lda1cv <- MASS::lda(Xt,t(Y), CV=TRUE)

ct <- table(t(Y), lda1cv$class)

sortida <- list(sortida,paste("Accuracy"))
sortida <- list(sortida,paste(capture.output(ct)))
sortida <- list(sortida,paste(capture.output(prop.table(ct, 1))))
sortida <- list(sortida,paste(capture.output(prop.table(ct))))
sortida <- list(sortida,paste("Accuracy",capture.output(sum(diag(prop.table(ct))))))

################################

# analysing the discriminant index
plda1 <- predict(lda1,Xt)

DF <- data.frame()
#head(DF)
if (B3 == TRUE) {
  if(plyr::empty(DF)){
    DF <- as.data.frame(plda1$x)
  } else{
    DF <- cbind.data.frame(DF, plda1$x)
  }
}
if (B4 == TRUE) {
  if(plyr::empty(DF)){
    DF <- as.data.frame(plda1$class)
  } else{
    DF <- cbind.data.frame(DF, plda1$class)
  }
}
if (B5 == TRUE) {
  if(plyr::empty(DF)){
    DF <- as.data.frame(plda1$plda1$posterior)
  } else{
    DF <- cbind.data.frame(DF, plda1$posterior)
  }
}

nam <- levels(plda1$class)
sortida <- list(sortida,paste("Analysis of discriminant index"))
sortida <- list(sortida,paste(capture.output(nam[1])))
sortida <- list(sortida,paste(capture.output(summary(plda1$x[plda1$class==nam[1]]))))
sortida <- list(sortida,paste(capture.output(nam[2])))
sortida <- list(sortida,paste(capture.output(summary(plda1$x[plda1$class==nam[2]]))))

name <- paste(tempdir(),'discriminant_index.png',sep="\\")
png(name)
plot(density(plda1$x[plda1$class==nam[1]]),
     xlim=c(-7,7),ylim=c(0,0.5),main="lda",xlab="discriminant index")
lines(density(plda1$x[plda1$class==nam[2]]),lty=2)
dev.off()
graphnames[2] <- name


################################

# uniform prior: fifty-fifty"

if (B1 == TRUE) {
  lda1Unif <- MASS::lda(Xt,t(Y),rep(0.5,nparts))
  
  sortida <- list(sortida,paste("Uniform prior"))
  sortida <- list(sortida,paste(capture.output(lda1Unif)))
  
  Orig <- cbind.data.frame(Xt,Y)
  nX <- ncol(X)
  midpoint <- (apply(Orig[Orig[,nparts+1]=="thol",1:nparts],2,mean) +apply(Orig[Orig[,nparts+1]=="calc",1:nparts],2,mean))/2
  sortida <- list(sortida,paste("Mid point"))
  sortida <- list(sortida,paste(capture.output(midpoint)))
  sortida <- list(sortida,paste("Prediction for mid point"))
  sortida <- list(sortida,paste(capture.output(predict(lda1Unif,midpoint))))
}


################################
#predicting for new samples

DF1 <- NULL

if (B2 == TRUE) {
  P1t <- coda.base::coordinates(Z, basis = coda.base::sbp_basis(BaseX))
  DF1 <- as.data.frame(predict(lda1,P1t))
}

################################


# Output
rm(cdp_res)
cdp_res = list(
  'text' = sortida,
  'dataframe' = list(),
  'graph' = graphnames,
  'new_data' = data.frame(DF),
  'new_data2' = data.frame(DF1)
)


