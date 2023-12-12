cdp_check = function(){
  if(!exists('X')) return("No data has been selected")
  if(!exists('GROUP')) return("No group has been selected")
  if(length(table(GROUP)) <= 1) return("Grouping variable needs to have two or more categories")
  cond1 = cdp_check_compositional(X)
  if(!is.null(cond1)) return(cond1)
}
cdp_analysis = function(){
  ################# MAIN #################
  
  # save.image('Rscripts/cdp_discriminant_analysis.RData')
  H = coda.base::coordinates(X)
  prior = as.vector(prop.table(table(GROUP)))
  if(V1){
    prior = rep(1/length(prior), length(prior))
  }
  
  lda1 = MASS::lda(x = H, grouping = as.factor(GROUP), prior = prior)
  lda1_coda = lda1
  lda1_coda$means = coda.base::composition(lda1$means, coda.base::basis(H))
  lda1_coda$scaling = t(coda.base::composition(t(lda1$scaling), coda.base::basis(H)))
  
  
  lda_coef = coef(lda1)
  if(NCOL(lda_coef) == 1){
    lda1.dfunc = sprintf("\nDiscriminant function:\n%s = 0", paste(sprintf("%0.3f ln %s", coda.base::basis(H) %*% lda_coef, colnames(X)), collapse = ' + '))
    lda1.dfunc = gsub("+ -", "- ", lda1.dfunc, fixed = TRUE)
  }else{
    lda1.dfunc = "\nDiscriminant functions:"
    for(j in 1:NCOL(lda_coef)){
      lda1.dfunc = sprintf("%s\n%s = 0", 
                           lda1.dfunc, 
                           paste(sprintf("%0.3f ln %s", coda.base::basis(H) %*% lda_coef[,j], colnames(X)), collapse = ' + '))
    }
    lda1.dfunc = gsub("+ -", "- ", lda1.dfunc, fixed = TRUE)
  }
  
  
  lda1cv = MASS::lda(H, GROUP, prior = prior, CV = TRUE)
  
  sortida <- paste(capture.output(lda1_coda))
  sortida = c(sortida, lda1.dfunc)
  ct <- table(GROUP, lda1cv$class)
  
  sortida <- c(sortida,"\nLOOCV table:")
  sortida <- c(sortida,capture.output(ct))
  sortida <- c(sortida,sprintf("\nAccuracy: (%s)/%d = %.4f", 
                               paste(diag(ct), collapse = '+'),
                               sum(ct),
                               sum(diag(prop.table(ct)))))
  
  plda1 = predict(lda1, H)
  DF = NULL
  if(V2){
    DF = as.data.frame(plda1$x)
  }
  if(V3){
    if(is.null(DF)) DF = as.data.frame(plda1$posterior)
    DF = cbind(DF, as.data.frame(plda1$posterior))
  }
  if(V4){
    if(is.null(DF)) DF = data.frame(class = as.vector(plda1$class))
    DF$class = as.vector(plda1$class)
  }
  
  graphname = sprintf("%s.pdf", tempfile())
  svg(filename = graphname, width = PLOT_WIDTH, height = PLOT_HEIGTH)
  
  plot(lda1, dimen=1, type="both")
  dev.off()

  ################################
  # Output
  res = list(
    'text' = unlist(sortida),
    'dataframe' = list(),
    'graph' = graphname,
    'new_data' = data.frame(DF)
  )
  
  if(exists('X_new')){
    H_new = coda.base::coordinates(X_new, coda.base::basis(H))
    new_data2 = data.frame(predict(lda1, newdata = as.data.frame(H_new))$class)
    names(new_data2) = paste0('group.pred')
    res[['new_data2']] = new_data2
  }
  
  res
}

