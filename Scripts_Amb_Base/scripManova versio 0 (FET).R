################################
# Script for compositional manova
# X: Compositional Matrix
# Y: categorical variable
# B1: Residuals
# B2: Analyze differences between pairs of groups
# BaseX: Matrix containing SBP
# Menu S3

#load("L:/CoDaCourse/CoDaCourse 17/Material/LABS 2017/Scripts 2018/CoDaLabs/Scripts CoDaPack/activManova.RData") #name = nom de l'arxiu de dades
#X <- as.data.frame(dpolen[,1:3])
#Y <- as.data.frame(dpolen[,4])
#B1 <- as.logical(TRUE)
#B2 <- as.logical(TRUE)
#BaseX = matrix(c(1, 1, -1, 1, -1, 0), ncol = 2)
#library(coda.base)

if (is.factor(Y) == FALSE) {
  Y <- as.factor(as.matrix(Y))
}

nOrig <- length(X)
nobs <- nrow(X)
Xt <- coda.base::coordinates(X, basis = "ilr", label = "ilr.", sparse_basis = FALSE)
#Xt <- coda.base::coordinates(X, basis = coda.base::sbp_basis(BaseX))
nparts=length(Xt)
for (n in 1:nparts)
{
  colnames(Xt)[n] <- paste("ilr.",n,sep="")
}
#head(Xt)

# appling the function manova(): the argument should be a matrix and

# introduced as a formula

mva<-manova(as.matrix(Xt)~Y)
#rm(sortida)
sortida <- list(paste("MANOVA ANALYSIS"),
                paste(capture.output(summary(mva,test="Wilks"))),
                paste(capture.output(summary(mva,test="Pillai")) ),
                paste(capture.output(summary(mva,test="Hotelling"))),
                paste(capture.output(summary(mva,test="Roy"))))

################################

# centers of each group ci and whole center 

#int = as.integer(Y)
#nCat = nlevels(as.factor(Y)) 
#Num <- matrix(nrow = nCat, ncol = ncol(X))
#Den <- matrix(nrow = nCat, ncol = ncol(X))
#colnames(Num)<-colnames(X)
#colnames(Den)<-colnames(X)
#rownames(Num)<-levels(Y)
#rownames(Den)<-levels(Y)
#for(i in 1:nCat) {
#  Num[i,]<-exp(colMeans(log(X[int==i,])))/sum(exp(colMeans(log(X[int==i,]))))
#  Den[i,]<-exp(colMeans(log(X)))/sum(exp(colMeans(log(X))))
#}

#sortida <- list(sortida,paste("Centers of each group"))
#sortida <- list(sortida,paste(capture.output(Num)))
#sortida <- list(sortida,paste("Whole center"))
#sortida <- list(sortida,paste(capture.output(Den[1,])))

################################

# a bar plot to compare all the centers with the whole center

#rm(graphnames)
#graphnames <- list()
#name [1]<- paste("Bar_plot_comparing_all_the_centers_with_the_whole_center.png",sep="")
name <- list()
name[1] <- "Nom columna 1" # PREGUNTAR NOM DE LES COLUMNES A EN SANTI!!!! (Residuals?)
name[2] <- "Nom columna 2" # PREGUNTAR ELS NOMS DE LES COLUMNES A EN SANTI!!!! (Dif. between...?)
#png(name)
#barplot(log(Num/Den), beside=TRUE,col=c(1:nCat))
#dev.off()
#graphnames[1] <- name


################################

# Between, Within and Total sum of squares matrices

sortida <- list(sortida,paste("Between and Within sum of squares matrices"))
sortida <- list(sortida,paste(capture.output(summary(mva)$SS)))
sortida <- list(sortida,paste("Total sum of squares matrices"))
sortida <- list(sortida,paste(capture.output(cov(Xt[,1:nparts])*(nobs-1))))

################################

#take the residuals of the model and add them to the data set

######### PART NOVA
DF  <-  data.frame()
######### FI PART NOVA

if (B2 == TRUE) {
  DF <- data.frame()
  for (n in 1:nparts)
  {
    colnames(mva$residuals)[n] <- paste(names(Xt[n]),".r",sep="")
  }
  DF <- as.data.frame(mva$residuals)
}


################################
######################
##########
################################

# Bonferroni correction:


if (B2 == TRUE) {
  int <- as.integer(Y)
  Mat <- cbind.data.frame(Xt,int)
  n <- 2
  for (i in 1:(nCat-1)) 
    for (j in (i+1):nCat)
    {
      mva2<-manova(as.matrix(Xt)~Y,subset=(int %in% c(i,j)))
      sortida <- list(sortida,paste("comparison between ",i," and ",j))
      sortida <- list(sortida,paste(capture.output(summary(mva2,test="Wilks"))))
      #      name <- paste("Bar_plot_comparing_pairs_of_centers_",i, "_and_",j,".png",sep="")
      #      png(name)
      #      barplot(log(cmat[i,]/cmat[j,]))
      #      dev.off()
      #      graphnames[n] <- name
      n <- n+1
    }
}

# Output
#rm(cdp_res)
cdp_res = list(
  'text' = unlist(sortida),
  'dataframe' = list(),
  #  'graph' = graphnames,
  'graph' = list(),
  'new_data' = DF
)

