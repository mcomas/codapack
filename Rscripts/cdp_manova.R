# save.image('Rscripts/cdp_manova.RData')

H = coda.base::coordinates(X, basis = 'ilr')

mva<-manova(H~GROUP)
output = "<p><b>Manova table</b></p>"
output = c(output, gsub("[‘’]", "'", capture.output(summary(mva, test = V3))))


################################

# Between, Within and Total sum of squares matrices
SSTreatment = unname(summary(mva)$SS$GROUP)
SSResiduals = unname(summary(mva)$SS$Residuals)
SSTotals = unname(cov(H)*(nrow(H)-1))

nl = ceiling((ncol(SSTotals)-1)/2)

format_matrix = function(X, pattern = "%0.3f", header = NULL){
  str_X = apply(X, 1, function(x) sprintf(pattern, x))
  nc = sprintf("%% %ds", max(nchar(str_X)) + 1)
  if(is.null(header)){
    str_X = sprintf("|%s|", apply(str_X, 1, function(x) paste(sprintf(nc, x), collapse = ' ')))
  }else{
    str_X = sprintf("|%s|", apply(rbind(header, '-', str_X), 1, function(x) paste(sprintf(nc, x), collapse = ' ')))
  }
}  
MSSB = format_matrix(SSTreatment)
SUM = c(rep("   ", length(MSSB)-nl-1), " + ", rep("   ", nl))
MSSW = format_matrix(SSResiduals)
EQUAL = c(rep("   ", length(MSSB)-nl-1), " = ", rep("   ", nl))
MSST = format_matrix(SSTotals)
output = c(output, "<br/><b>Orthonormal Basis:</b><br />")
output = c(output, format_matrix(sign(attr(H, 'basis')), "%d", header = colnames(X)))
output = c(output, "<br /><b>Sum of Square Decompositions</b>")
output = c(output, "<br />SSTreatment + SSResiduals = SSTotal<br />")
output = c(output, paste0(MSSB, SUM, MSSW, EQUAL, MSST))
output = c(output, sprintf("<br/>R² = 1 - trace(SSResiduals) / trace(SSTotal) = %0.2f", 
                           sum(diag(SSTreatment)) / sum(diag(SSTotals))))

df.residuals = list()
if(V1){
  CH = coda.base::composition(mva$residuals, basis = 'ilr')
  df.residuals = as.data.frame(apply(CH, 2, identity))
  names(df.residuals) = paste0(names(df.residuals), '.res')
}

if(V2){
  output = c(output, "<br /><b>Pair comparison</b>")
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

cdp_res = list(
  'text' = output,
  'dataframe' = list(),
  'graph' = list(),
  'new_data' = df.residuals
)

