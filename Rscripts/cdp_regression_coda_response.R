cdp_check = function(){
  if(!exists('X')) return("No data has been selected")
  if(!exists('Y')) return("No response has been selected")
  
  cond1 = cdp_check_compositional(Y)
  if(!is.null(cond1)) return(cond1)
  
  cond2 = nrow(BasisY) == ncol(Y)
  if(!cond2) return("Basis not defined")
  
}
cdp_analysis = function(){
  # save.image("Rscripts/cdp_regression_coda_response.RData")
  
  H = coda.base::coordinates(Y, basis = coda.base::sbp_basis(BasisY))
  colnames(H) = paste0('olr.', 1:ncol(H))
  
  text_output = cdp_print_sbp(BasisY, colnames(Y))
  # nccol = pmax(3, nchar(colnames(Y)))
  # text_output = c("Partition:", capture.output({
  #   cat(sprintf(sprintf("%%%ds", nccol), colnames(Y)), "\n")
  #   cat(apply(matrix(sprintf(sprintf("%%%dd", nccol), BasisY), byrow = TRUE, ncol = ncol(Y)),
  #             1,
  #             paste, collapse=' '), sep='\n')
  # }))
  
  
  str_y = colnames(H)
  if(ncol(H) > 1) str_y = sprintf("cbind(%s)", paste(colnames(H), collapse=','))
  str_x = paste(colnames(X), collapse='+')
  str_frm = sprintf("%s ~ %s", str_y, str_x)
  
  . = as.data.frame(cbind(H,X))
  LM = eval(parse(text = sprintf("lm(%s, .)", str_frm)))
  
  text_output = c(text_output, "\nCall:")
  text_output = c(text_output, deparse(LM$call))
  
  SLM = summary(LM)
  if(ncol(H) == 1) SLM = list(SLM)
  nr = length(SLM) * nrow(SLM[[1]]$coefficients)
  COEFS = matrix(0, nrow = nr, ncol = 4)
  for(i in 1:length(SLM)){
    COEFS[seq(i, nr, length(SLM)),] = SLM[[i]]$coefficients
  }
  longest_name = max(nchar(rownames(SLM[[1]]$coefficients)))
  rnames = rep("", nr)
  rnames[seq(1, nr, length(SLM))] = rownames(SLM[[1]]$coefficients)
  rnames = sprintf(sprintf("%%-%ds", longest_name), rnames)
  rnames = paste(rnames, colnames(H))
  colnames(COEFS) = colnames(SLM[[1]]$coefficients)
  rownames(COEFS) = rnames

  text_output = c(text_output, "\nCoefficients:")
  text_output = c(text_output, gsub("\"", "", capture.output(printCoefmat(COEFS))))
  
  
  R2 = sum(scale(LM$fitted.values, scale = FALSE)^2) / sum(scale(H, scale = FALSE)^2) 
  if(ncol(H) == 1){
    RES = as.matrix(anova(update(LM, .~1), LM))[2,c('F', 'Df', 'Res.Df', 'Pr(>F)')]
  }else{
    F.APPROX = "(Wilks' approx.)"
    RES = unlist(anova(LM, update(LM, .~1), test = 'Wilks')[2,c('approx F', 'num Df' , 'den Df' ,'Pr(>F)')])
  }
  

  # https://stackoverflow.com/questions/32342018/summary-lm-output-customization
  col_fw = function(col, align = 'left'){
    if(align == 'left'){
      pat = sprintf("%%-%ds", max(nchar(col)))
    }else{
      pat = sprintf("%%%ds", max(nchar(col)))
    }
    
    col = sprintf(pat, col)
    col
  }
  summ.title = c("", paste("Response", colnames(H)))
  summ.r2 = c("R-squared:", sapply(SLM, function(x) formatC(x$r.squared)))
  summ.fstat = c("F-statistic:", 
                sapply(SLM, function(x) paste(formatC(x$fstatistic[1L]), "on", x$fstatistic[2L], "and",
                                              x$fstatistic[3L], "DF,  p-value:", 
                                              format.pval(pf(x$fstatistic[1L],
                                                             x$fstatistic[2L], 
                                                             x$fstatistic[3L], 
                                                             lower.tail = FALSE)))))
  if(ncol(H) > 1){
    summ.title = c(summ.title, 'Overall')
    summ.r2 = c(summ.r2, formatC(R2))
    summ.fstat = c(summ.fstat,
                   paste(formatC(RES[1L]), "on", RES[2L], "and",
                         RES[3L], "DF,  p-value:", 
                         format.pval(pf(RES[1],
                                        RES[2], RES[3], lower.tail = FALSE)), 
                         F.APPROX))
  }
  col1 = col_fw(summ.title)
  col2 = col_fw(summ.r2)
  col3 = col_fw(summ.fstat)
  text_output = c(text_output, "", capture.output({
    cat(col1[1], "\t",  col2[1], "\t", col3[1], "\n")
    for(i in 1:length(SLM)){
      x = SLM[[i]]
      cat(col1[1+i], "\t", col2[1+i], "\t", col3[1+i], "\n")
    }
    if(ncol(H)>1){
      cat(col1[length(SLM)+2], "\t",  col2[length(SLM)+2], "\t", col3[length(SLM)+2], "\n")
    }
  }))
  
  new_data = list()
  if (V1) new_data = c(new_data, setNames(apply(LM$residuals, 2, identity, simplify = FALSE), paste0('r.', colnames(H))))
  if (V2) new_data = c(new_data, setNames(apply(LM$fitted.values, 2, identity, simplify = FALSE), paste0('f.', colnames(H))))

  graphnames <- replicate(ncol(H), sprintf("%s.svg", tempfile()))
  for(i in seq_along(graphnames)){
    str_frm_sub = sprintf("%s ~ %s", colnames(H)[i], str_x)
    LM_sub = eval(parse(text = sprintf("lm(%s, .)", str_frm_sub)))
    svg(graphnames[i], width = PLOT_WIDTH, height = PLOT_HEIGTH)
    oldpar <- par(oma=c(0,0,3,0), mfrow=c(2,2))
    plot(LM_sub,sub.caption=deparse(as.formula(LM_sub)))  # Plot the model information
    par(oldpar)
    dev.off()
  }
  names(graphnames) = paste("Response", colnames(H))

  NDF = cbind(
    data.frame("Variable" = row.names(LM$coefficients)), 
    as.data.frame(LM$coefficients, row.names = FALSE)
  )

  if(exists('X_new')){
    new_data2 = data.frame(predict(LM, newdata = as.data.frame(X_new)))
    new_data2_coda = coda.base::composition(new_data2, coda.base::sbp_basis(BasisY))
    names(new_data2_coda) = paste0(colnames(Y), '.pred')
  }
  
  # Ooutput
  cdp_out = list(
    'text' = gsub("[‘’]", "'", text_output),
    'dataframe' = list('coefficients' = NDF),
    'graph' = graphnames,
    'new_data' = new_data
  )
  if(exists('X_new')){
    cdp_out[['new_data2']] = new_data2_coda
  }
  cdp_out
}
