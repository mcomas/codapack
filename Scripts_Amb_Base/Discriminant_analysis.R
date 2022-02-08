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

################# LIBRARIES #################

#install.packages("zCompositions")
library(zCompositions)
library(coda.base)

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

BASIS = coda.base::sbp_basis(BaseX)
#Xt <- coda.base::coordinates(X, basis = "ilr", label = "ilr.", sparse_basis = FALSE)
Xt <- coda.base::coordinates(X, basis = BASIS, label = 'ilr.')
nparts=NCOL(X)

prior = as.vector(prop.table(table(Y)))
if(B1){
  prior = prop.table(rep(1, length(prior)))
}


lda1 <- MASS::lda(Xt,t(Y), prior = prior)
plda1 <- predict(lda1, Xt)
lda1.dfunc = sprintf("Discriminant function:\n\n%s = 0", paste(sprintf("%0.3f ln %s", sbp_basis(BaseX) %*% coef(lda1), names(X)), collapse = '+'))
lda1.dfunc = gsub("+-", "-", lda1.dfunc, fixed = TRUE)

lda1cv = MASS::lda(Xt,t(Y), prior = prior, CV = TRUE)


sortida <- paste(capture.output(lda1))
#sortida = capture.output(knitr::kable(cbind(t(lda1$means), lda1$scaling), 'html'))
sortida = c(sortida, lda1.dfunc)
ct <- table(t(Y), lda1cv$class)

sortida <- c(sortida,"Cross table:")
sortida <- c(sortida,capture.output(ct))
# sortida <- c(sortida,capture.output(prop.table(ct, 1)))
# sortida <- c(sortida,capture.output(prop.table(ct)))
sortida <- c(sortida,sprintf("Accuracy:\n\n (%s)/%d = %.4f", 
                             paste(diag(ct), collapse = '+'),
                             sum(ct),
                             sum(diag(prop.table(ct)))))

DF1 = NULL
if(B2){
  P1t <- coda.base::coordinates(Z, basis = coda.base::sbp_basis(BaseX), label = 'ilr.')
  DF1 <- as.data.frame(predict(lda1,newdata=P1t))
}
DF = NULL
if(B3){
  DF = as.data.frame(plda1$x)
}
if(B4){
  if(is.null(DF)) DF = data.frame(class = as.vector(plda1$class))
  DF$class = as.vector(plda1$class)
}
if(B5){
  if(is.nulll(DF)) DF = as.data.frame(plda1$posterior)
  DF = cbind(DF, as.data.frame(plda1$posterior))
}
#rm(sortida)


################################

# visualizing the results: panels of histograms and overlayed density plots

# for 1st discriminant function

#rm(graphnames)
graphnames <- list()
name <- generateFileName(paste(tempdir(),'panels_of_histograms_and_overlayed_density_plots',sep="\\"))
svg(name)
plot(lda1, dimen=1, type="both")
dev.off()
graphnames[1] <- name


################################





nam <- levels(plda1$class)
sortida <- list(sortida,paste("Analysis of discriminant index:"))
sortida <- list(sortida,paste(nam[1]))
sortida <- list(sortida,paste(capture.output(summary(plda1$x[plda1$class==nam[1]]))))
sortida <- list(sortida,paste(nam[2]))
sortida <- list(sortida,paste(capture.output(summary(plda1$x[plda1$class==nam[2]]))))

name <- generateFileName(paste(tempdir(),'discriminant_index',sep="\\"))
svg(name)
plot(density(plda1$x[plda1$class==nam[1]]),
     xlim=c(-7,7),ylim=c(0,0.5),main="lda",xlab="discriminant index")
lines(density(plda1$x[plda1$class==nam[2]]),lty=2)
dev.off()
graphnames[2] <- name



################################

# Output
cdp_res = list(
  'text' = unlist(sortida),
  'dataframe' = list(),
  'graph' = graphnames,
  'new_data' = data.frame(DF),
  'new_data2' = data.frame(DF1)
)


