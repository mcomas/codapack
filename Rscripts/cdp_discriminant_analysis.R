save.image('Rscripts/cdp_discriminant_analysis.RData')

################# MAIN #################


H = coda.base::coordinates(X)
prior = as.vector(prop.table(table(GROUP)))
if(V1){
  prior = rep(1/length(prior), length(prior))
}

lda1 = MASS::lda(x = H, grouping = as.factor(GROUP), prior = prior)
plda1 <- predict(lda1, H)

lda1.dfunc = sprintf("Discriminant function:\n\n%s = 0", paste(sprintf("%0.3f ln %s", coda.base::basis(H) %*% coef(lda1), colnames(X)), collapse = '+'))
lda1.dfunc = gsub("+-", "-", lda1.dfunc, fixed = TRUE)

lda1cv = MASS::lda(H, GROUP, prior = prior, CV = TRUE)


sortida <- paste(capture.output(lda1))
sortida = c(sortida, lda1.dfunc)
ct <- table(GROUP, lda1cv$class)

sortida <- c(sortida,"Cross table:")
sortida <- c(sortida,capture.output(ct))
# sortida <- c(sortida,capture.output(prop.table(ct, 1)))
# sortida <- c(sortida,capture.output(prop.table(ct)))
sortida <- c(sortida,sprintf("Accuracy:\n\n (%s)/%d = %.4f", 
                             paste(diag(ct), collapse = '+'),
                             sum(ct),
                             sum(diag(prop.table(ct)))))

# DF1 = NULL
# if(V2){
#   P1t <- coda.base::coordinates(Z, basis = coda.base::sbp_basis(BaseX), label = 'ilr.')
#   DF1 <- as.data.frame(predict(lda1,newdata=P1t))
# }
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




  # nam <- levels(plda1$class)
  # sortida <- list(sortida,paste("Analysis of discriminant index:"))
  # sortida <- list(sortida,paste(nam[1]))
  # sortida <- list(sortida,paste(capture.output(summary(plda1$x[plda1$class==nam[1]]))))
  # sortida <- list(sortida,paste(nam[2]))
  # sortida <- list(sortida,paste(capture.output(summary(plda1$x[plda1$class==nam[2]]))))
  # 
  # name <- generateFileName(paste(tempdir(),'discriminant_index',sep="\\"))
  # svg(name)
  # plot(density(plda1$x[plda1$class==nam[1]]),
  #      xlim=c(-7,7),ylim=c(0,0.5),main="lda",xlab="discriminant index")
  # lines(density(plda1$x[plda1$class==nam[2]]),lty=2)
  # dev.off()
  # graphnames[2] <- name



################################

# Output
cdp_res = list(
  'text' = unlist(sortida),
  'dataframe' = list(),
  'graph' = graphname,
  'new_data' = data.frame(DF)
)


