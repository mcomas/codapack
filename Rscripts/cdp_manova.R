save.image('Rscripts/cdp_manova.RData')

H = coda.base::coordinates(X)

mva<-manova(H~GROUP)
output = "<p><b>Manova table</b></p>"
output = c(output, gsub("[‘’]", "'", capture.output(summary(mva, test = V3))))


################################

# Between, Within and Total sum of squares matrices
SSTreatment = unname(summary(mva)$SS$GROUP)
SSResiduals = unname(summary(mva)$SS$Residuals)
SSTotals = unname(cov(H)*(nrow(H)-1))

nl = ceiling((ncol(SSTotals)-1)/2)

MSSB = capture.output(knitr::kable(SSTreatment, digits = 3, format = 'markdown'))
SUM = c(rep("   ", length(MSSB)-nl-1), " + ", rep("   ", nl))
MSSW = capture.output(knitr::kable(SSResiduals, digits = 3, format = 'markdown'))
EQUAL = c(rep("   ", length(MSSB)-nl-1), " = ", rep("   ", nl))
MSST = capture.output(knitr::kable(SSTotals, digits = 3, format = 'markdown'))
# output = c(output, "<br/><b>Orthonormal Basis:</b><br/>")
# output = c(output, knitr::kable(sign(coda.base::basis(H))))
# output = c(output, "<br/><b>Sum of Square Decompositions</b>")
# output = c(output, "<br />SSTreatment + SSResiduals = SSTotal")
# output = c(output, paste0(MSSB, SUM, MSSW, EQUAL, MSST))
output = c(output, sprintf("<br/>R² = 1 - trace(SSResiduals) / trace(SSTotal) = %0.2f", 
                           sum(diag(SSTreatment)) / sum(diag(SSTotals))))

df.residuals = list()
if(V1){
  df.residuals = as.data.frame(coda.base::composition(mva$residuals, coda.base::basis(H)))
  names(df.residuals) = paste0(names(df.residuals), '.res')
}

if(V2){
  output = c(output, "<br/><b>Pair comparison</b>")
  output = c(output, capture.output(
    knitr::kable(data.table::rbindlist(apply(combn(unique(GROUP), 2), 2, function(group){
      H.sub = subset(H, GROUP %in% group)
      GROUP.sub = GROUP[GROUP %in% group]
      mva.sub = manova(H.sub~GROUP.sub)
      data.frame('comparison' = paste(group, collapse = '~'),
                 'approx F' =summary(mva.sub, test = V3)$stats[1,3],
                 'p-value' = summary(mva.sub, test = V3)$stats[1,6])
    })), format = 'markdown')
    ))
}

# ######### PART NOVA
# DF  <-  data.frame()
# ######### FI PART NOVA
# 
# if (B1 == TRUE) {
#   DF <- data.frame()
#   for (n in 1:nparts)
#   {
#     colnames(mva$residuals)[n] <- paste(names(Xt[n]),".r",sep="")
#   }
#   DF <- as.data.frame(mva$residuals)
# }
# 
# 
# ################################
# ######################
# ##########
# ################################
# 
# # Bonferroni correction:
# 
# 
# if (B2 == TRUE) {
#   int <- as.integer(Y)
#   Mat <- cbind.data.frame(Xt,int)
#   n <- 2
#   for (i in 1:(nCat-1)) 
#     for (j in (i+1):nCat)
#     {
#       mva2<-manova(as.matrix(Xt)~Y,subset=(int %in% c(i,j)))
#       sortida <- list(sortida,paste("comparison between ",i," and ",j))
#       sortida <- list(sortida,paste(capture.output(summary(mva2,test="Wilks"))))
#       #      name <- paste("Bar_plot_comparing_pairs_of_centers_",i, "_and_",j,".png",sep="")
#       #      png(name)
#       #      barplot(log(cmat[i,]/cmat[j,]))
#       #      dev.off()
#       #      graphnames[n] <- name
#       n <- n+1
#     }
# }

# Output
#rm(cdp_res)
cdp_res = list(
  'text' = output,
  'dataframe' = list(),
  #  'graph' = graphnames,
  'graph' = list(),
  'new_data' = df.residuals
)

