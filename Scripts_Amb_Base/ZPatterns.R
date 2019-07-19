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
if(.Platform$OS.type == "unix") {
  library(cairoDevice)
  svg = Cairo_svg
}

save.image('temp.RData')
graphnames <- list()
name <- file.path(tempdir(), 'Zero_Pattern.svg') # paste(tempdir(),"Zero_Pattern.svg",sep="\\")
svg(name)
sortida <- capture.output(zCompositions::zPatterns(X,label=P1,show.means=B1,bar.labels=B2))
dev.off()
graphnames[1] <- name

#printGraphics <- function(width, height){
#
#    png(graphnames[1], width, height)
#    zCompositions::zPatterns(X,label=P1,show.means=B1,suppress.print=TRUE,bar.labels=B2)
#    dev.off()
#}

# Ooutput
cdp_res = list(
  'text' = list(paste("ZERO PATTERNS"),sortida),
  'dataframe' = list(),
  'graph' = graphnames,
  'new_data' = list()
)

