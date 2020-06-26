#################################
# Scatterplot script incloding Groups
# X Matrix (2 or 3 variables)
# Y: Categorical variable
# Menu S3


######### LIBRARIES ############

library(scatterplot3d)

######### FUNCTIONS ############

generateFileName <- function(candidateName){
  name = candidateName
  nameFile = paste(name, ".svg", sep = "")
  
  while(file.exists(nameFile)){
    name = paste(name, "c", sep = "")
    nameFile = paste(name, ".svg", sep = "")
  }
  
  return(nameFile)
}

######### MAIN ############

graphnames <- list()
name <- generateFileName(paste(tempdir(), "Scatterplot", sep = "\\"))
svg(name)

if(is.null(Y)){ # Without group
  if(length(X) == 2){ # 2D Scatterplot
    if(B1){plot(X, main = "Scatterplot 2D", pch = 19, xlim = c(min(X), max(X)), ylim = c(min(X), max(X)))}
    else{plot(X, main = "Scatterplot 2D", pch = 19)}
  }else{ # 3D Scatterplot
    if(B1){scatterplot3d(X, pch = 19, xlim = c(min(X), max(X)), ylim = c(min(X), max(X)), zlim = c(min(X), max(X)), color = "black")}
    else{scatterplot3d(X, pch = 19, color = "black")}
  }
}else{ # With group
  Y <- as.factor(as.matrix(Y)) # TRANSFORMACIÓ DE LA Y
  if(length(X) == 2){ # 2D Scatterplot
    if(B1){plot(X, col = Y, main = "Scatterplot 2D", pch = 19, xlim = c(min(X), max(X)), ylim = c(min(X), max(X)))}
    else{plot (X, col = Y, main = "Scatterplot 2D", pch = 19)}
    legend("topright", legend = levels(Y), pch = 19, col = unique(Y))
  }else{ # 3D Scatterplot
    if(B1){scatterplot3d(X, color = as.numeric(Y), pch = 19, xlim = c(min(X), max(X)), ylim = c(min(X), max(X)), zlim = c(min(X), max(X)))}
    else{scatterplot3d(X, color = as.numeric(Y), pch = 19)}
    legend("topright", legend = levels(Y), pch = 19, col = unique(Y))
  }
}

dev.off() # tanquem svg després de fer el plot
graphnames[1] <- name

######### OUTPUT ############

cdp_res = list(
  'text' = list(),
  'dataframe' = list(),
  'graph' = graphnames,
  'new_data' = list()
)