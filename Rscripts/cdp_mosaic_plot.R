# R script for mosaic plots with Menu S4. 
# Enter one categorical variable for the horizontal axis in "Selected data X"
# And one for the vertical axis in "Selected data Y".

generateFileName <- function(candidateName){
  name = candidateName
  nameFile = paste(name, ".svg", sep = "")
  while(file.exists(nameFile)){
    name = paste(name, "c", sep = "")
    nameFile = paste(name, ".svg", sep = "")}
  return(nameFile)}

graphnames <- list()
name <- generateFileName(paste(tempdir(), "Mosaic_plot", sep = "\\"))
svg(name)
plot(as.factor(as.matrix(X)),as.factor(as.matrix(Y)),
     xlab=colnames(X),ylab=colnames(Y))
dev.off()
graphnames[1] <- name

cdp_res = list(
  'text' = unlist(list(paste("MOSAIC PLOT"))),
  'dataframe' = list(),
  'graph' = graphnames,
  'new_data' = list())
