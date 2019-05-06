# Script for Zero Patterns
# X: compositional matrix
# B1: Show Means (Default = FALSE)
# P1: label for unobserved values (default = NaN)
# B2: Show Percentages (default = FALSE) 
# Menu S1

#load("L:/CoDaCourse/CoDaCourse 17/Material/LABS 2017/Scripts 2018/CoDaLabs/Scripts CoDaPack/activmiss.RData")
#X <- as.data.frame(dmiss[,1:5])
#B1 <- as.logical(TRUE)
#B2 <- as.logical(TRUE)
#P1 <- NaN

#library(zCompositions)

#rm(graphnames)
graphnames <- list()
name <- paste(tempdir(),"Zero_Pattern.png",sep="\\")
png(name)
zCompositions::zPatterns(X,label=P1,show.means=B1,suppress.print=TRUE,bar.labels=B2)
dev.off()
graphnames[1] <- name

# Ooutput
cdp_res = list(
  'text' = list(paste("ZERO PATTERNS"),
                paste(zCompositions::zPatterns(X,label=P1,show.means=B1,plot=FALSE))),
  'dataframe' = list(),
  'graph' = graphnames,
  'new_data' = list()
)

