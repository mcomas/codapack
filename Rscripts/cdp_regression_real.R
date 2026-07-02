cdp_check = function(){
  if(!exists('X')) return("No data has been selected")
  if(!exists('Y')) return("No response has been selected")
  
}
cdp_analysis = function(){
  # save.image("Rscripts/cdp_regression_real.RData")
  str_y = colnames(Y)
  if(NCOL(Y) > 1) str_y = sprintf("cbind(%s)", paste(colnames(Y), collapse=','))
  str_x = paste(colnames(X), collapse='+')
  str_frm = sprintf("%s ~ %s", str_y, str_x)
  
  . = as.data.frame(cbind(Y,X))
  LM = eval(parse(text = sprintf("lm(%s, .)", str_frm)))
  
  text_output = c(paste("LINEAR REGRESSION"),
                  paste("Dependent variable"),
                  paste(capture.output(cat(paste(colnames(Y), collapse=', ')))),
                  paste("Explanatory variables"),
                  paste(capture.output(cat(paste(colnames(X), collapse=', ')))),
                  "\nCall:",
                  deparse(LM$call))
  
  SLM = summary(LM)
  if(NCOL(Y) == 1) SLM = list(SLM)
  nr = length(SLM) * nrow(SLM[[1]]$coefficients)
  COEFS = matrix(0, nrow = nr, ncol = 4)
  for(i in 1:length(SLM)){
    COEFS[seq(i, nr, length(SLM)),] = SLM[[i]]$coefficients
  }
  longest_name = max(nchar(rownames(SLM[[1]]$coefficients)))
  rnames = rep("", nr)
  rnames[seq(1, nr, length(SLM))] = rownames(SLM[[1]]$coefficients)
  rnames = sprintf(sprintf("%%-%ds", longest_name), rnames)
  rnames = paste(rnames, colnames(Y))
  colnames(COEFS) = colnames(SLM[[1]]$coefficients)
  rownames(COEFS) = rnames

  text_output = c(text_output, "\nCoefficients:")
  text_output = c(text_output, gsub("\"", "", capture.output(printCoefmat(COEFS))))
  
  if(NCOL(Y) > 1){
    Y_centered = scale(as.matrix(Y), scale = FALSE)
    R2 = sum(scale(LM$fitted.values, scale = FALSE)^2) / sum(Y_centered^2)
    F.APPROX = "(Wilks' approx.)"
    RES = unlist(anova(LM, update(LM, .~1), test = 'Wilks')[2,c('approx F', 'num Df' , 'den Df' ,'Pr(>F)')])
  }

  col_fw = function(col, align = 'left'){
    if(align == 'left'){
      pat = sprintf("%%-%ds", max(nchar(col)))
    }else{
      pat = sprintf("%%%ds", max(nchar(col)))
    }
    
    col = sprintf(pat, col)
    col
  }
  summ.title = c("", paste("Response", colnames(Y)))
  summ.r2 = c("R-squared:", sapply(SLM, function(x) formatC(x$r.squared)))
  summ.fstat = c("F-statistic:", 
                 sapply(SLM, function(x) paste(formatC(x$fstatistic[1L]), "on", x$fstatistic[2L], "and",
                                               x$fstatistic[3L], "DF,  p-value:", 
                                               format.pval(pf(x$fstatistic[1L],
                                                              x$fstatistic[2L], 
                                                              x$fstatistic[3L], 
                                                              lower.tail = FALSE)))))
  if(NCOL(Y) > 1){
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
      cat(col1[1+i], "\t", col2[1+i], "\t", col3[1+i], "\n")
    }
    if(NCOL(Y)>1){
      cat(col1[length(SLM)+2], "\t",  col2[length(SLM)+2], "\t", col3[length(SLM)+2], "\n")
    }
  }))
  
  graphnames <- replicate(NCOL(Y), sprintf("%s.svg", tempfile()))
  for(i in seq_along(graphnames)){
    str_frm_sub = sprintf("%s ~ %s", colnames(Y)[i], str_x)
    LM_sub = eval(parse(text = sprintf("lm(%s, .)", str_frm_sub)))
    svg(graphnames[i], width = PLOT_WIDTH, height = PLOT_HEIGTH)
    oldpar <- par(oma=c(0,0,3,0), mfrow=c(2,2))
    plot(LM_sub,sub.caption=deparse(as.formula(LM_sub)))  # Plot the model information
    par(oldpar)
    dev.off()
  }
  names(graphnames) = paste("Response", colnames(Y))
  
  new_data  <-  list()
  RESIDUALS = as.matrix(LM$residuals)
  FITTED = as.matrix(LM$fitted.values)
  colnames(RESIDUALS) = colnames(Y)
  colnames(FITTED) = colnames(Y)
  if(V1) new_data = c(new_data, setNames(apply(RESIDUALS, 2, identity, simplify = FALSE), paste0('r.', colnames(Y))))
  if(V2) new_data = c(new_data, setNames(apply(FITTED, 2, identity, simplify = FALSE), paste0('f.', colnames(Y))))
  
  if(exists('X_new')){
    new_data2 = data.frame(predict(LM, newdata = as.data.frame(X_new)))
    names(new_data2) = paste0(colnames(Y), '.pred')
  }
  
  COEF_DF = as.data.frame(as.matrix(LM$coefficients), row.names = FALSE)
  if(NCOL(Y) == 1) names(COEF_DF) = colnames(Y)
  NDF = cbind(
    data.frame("Variable" = row.names(as.matrix(LM$coefficients))), 
    COEF_DF
  )
  
  # Output
  cdp_out = list(
    'text' = gsub("[‘’]", "'", text_output),
    'dataframe' = list('coefficients' = NDF),
    'graph' = graphnames,
    'new_data' = new_data
  )
  if(exists('X_new')){
    cdp_out[['new_data2']] = new_data2
  }
  cdp_out
}
