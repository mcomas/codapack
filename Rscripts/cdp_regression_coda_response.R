cdp_check = function(){
  if(!exists('X')) return("No data has been selected")
  if(!exists('Y')) return("No response has been selected")
  
  cond1 = cdp_check_compositional(Y)
  if(!is.null(cond1)) return(cond1)
  
  cond2 = nrow(BasisY) == ncol(Y)
  if(!cond2) return("Basis not defined")
  
}
cdp_analysis = function(){
  save.image("Rscripts/cdp_regression_coda_response.RData")
  
  H = coda.base::coordinates(Y, basis = coda.base::sbp_basis(BasisY))
  colnames(H) = paste0('ilr.', 1:ncol(H))
  
  str_y = colnames(H)
  if(ncol(H) > 1) str_y = sprintf("cbind(%s)", paste(colnames(H), collapse=','))
  str_x = paste(colnames(X), collapse='+')
  str_frm = sprintf("%s ~ %s", str_y, str_x)
  
  . = as.data.frame(cbind(H,X))
  LM = eval(parse(text = sprintf("lm(%s, .)", str_frm)))
  
  R2 = sum(scale(LM$fitted.values, scale = FALSE)^2) / sum(scale(H, scale = FALSE)^2) 
  
  SLM = summary(LM)
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

  B = coef(LM)
  S = sqrt(diag(vcov(LM)))
  P = 2*pt(-abs(B/S), LM$df.residual)
  # O = sprintf("% 0.6f (%0.6f) p=%s", B, S, ifelse(P<0.0001, "<0.0001", sprintf("%0.5f", P)))
  # dim(O) = dim(B)
  # colnames(O) = colnames(B)
  # rownames(O) = rownames(B)
  
  RES = unlist(anova(LM, update(LM, .~1), test = 'Wilks')[2,c('approx F', 'num Df' , 'den Df' ,'Pr(>F)')])
  output = gsub("\"", "", capture.output(printCoefmat(COEFS)))
  output = c("", "Coefficients:", output)
  output = c(deparse(LM$call), output)
  output = c("Call:", output)
  output = c(output, "")
  col_fw = function(col, align = 'left'){
    if(align == 'left'){
      pat = sprintf("%%-%ds", max(nchar(col)))
    }else{
      pat = sprintf("%%%ds", max(nchar(col)))
    }
    
    col = sprintf(pat, col)
    col
  }
  col1 = col_fw(c("", paste("Response", colnames(H)), 'Overall'))
  col2 = col_fw(c("Multiple R-squared:", sapply(SLM, function(x) formatC(x$r.squared)), formatC(R2)), align = 'right')
  
  fstat = c("F-statistic:", 
            sapply(SLM, function(x) paste(formatC(x$fstatistic[1L]), "on", x$fstatistic[2L], "and",
      x$fstatistic[3L], "DF,  p-value:", format.pval(pf(x$fstatistic[1L],
                                                        x$fstatistic[2L], x$fstatistic[3L], lower.tail = FALSE)))),
      paste(formatC(RES[1L]), "on", RES[2L], "and",
            RES[3L], "DF,  p-value:", format.pval(pf(RES[1],
                                                              RES[2], RES[3], lower.tail = FALSE)), "(Wilks' approx.)"))
  col3 = col_fw(fstat)
  output = c(output, capture.output({
    cat(col1[1], "\t",  col2[1], "\t", col3[1], "\n")
    for(i in 1:length(SLM)){
      x = SLM[[i]]
      cat(col1[1+i], "\t", col2[1+i], "\t", col3[1+i], "\n")
    }
    cat(col1[length(SLM)+2], "\t",  col2[length(SLM)+2], "\t", col3[length(SLM)+2], "\n")
  }))
  # PERF = matrix("", nrow = 3, ncol = 1+length(SLM))
  # oR2 = c(sapply(SLM, function(x) formatC(x$r.squared)), 
  #         formatC(R2))
  # oR2
  # cat("Multiple R-squared: ", sapply(SLM, function(x) formatC(x$r.squared)))
  # oF = c(sapply(SLM, function(x) formatC(formatC(x$fstatistic[1L]))), 
  #        formatC(formatC(RES[1])))
  # 
  # 
  # PERF[1, 1:length(SLM)] = sapply(SLM, function(x) formatC(x$r.squared))
  # PERF[1,1+length(SLM)] = formatC(R2)
  # PERF[2, 1:length(SLM)] = 
  # PERF[2,1+length(SLM)] = sprintf("%s (Wilks' approx.)", formatC(formatC(RES[1])))
  # PERF[3, 1:length(SLM)] = sapply(SLM, function(x) format.pval(pf(x$fstatistic[1L],
  #                                                                 x$fstatistic[2L], 
  #                                                                 x$fstatistic[3L], lower.tail = FALSE)))
  # PERF[3,1+length(SLM)] = format.pval(RES[4L])
  # rownames(PERF) = c("Multiple R-squared", "F-statistics", "p-value")
  # colnames(PERF) = c(colnames(H), 'Overall')
  # output = c(output, capture.output(PERF))
  #output = c(output, sprintf("Multiple R-squared:  %0.3f", R2))
  #output = c(output, sprintf("F-statistic (Wilks' approx.): %0.3f on %.1f and %.1f DF,  p-value: %0.5f", RES[1], RES[2], RES[3], RES[4]))
  # cat(output, sep ='\n')
  
  new_data = list()
  if (V1) new_data = c(new_data, setNames(apply(LM$residuals, 2, identity, simplify = FALSE), paste0('r.', colnames(H))))
  if (V2) new_data = c(new_data, setNames(apply(LM$fitted.values, 2, identity, simplify = FALSE), paste0('f.', colnames(H))))
  
  
  
  
  
  # # Create graphs
  # graphnames <- list()
  # for (n in 1:nparts)
  # {
  #   name <- generateFileName(paste(tempdir(),paste("Plots_of_residuals_",names(H[n]),sep=""),sep="\\"))
  #   svg(name)
  #   LM.temp <- lm(as.matrix(H[n])~as.matrix(X))
  #   oldpar <- par(oma=c(0,0,3,0), mfrow=c(2,2))
  #   title <- paste(names(H[n])," ~ ",names(X))
  #   plot(LM.temp,sub.caption=title)  # Plot the model information
  #   par(oldpar)
  #   dev.off()
  #   graphnames[n] <- name
  # }
  
  
  
  #PART NOVA. ARA TE EN COMPTE ELS NOMS DE TOTES LES X I TRANSPOSA COEFICIENTS PER PODER-LOS INVERTIR
  NDF<-as.data.frame(as.factor(c("intercept",names(X))))
  names(NDF)<-paste("Coefficients")
  NDF=cbind.data.frame(NDF,LM$coefficients)
  #FI PART NOVA.
  
  
  # Ooutput
  list(
    'text' = gsub("[‘’]", "'", output),
    'dataframe' = list(), #list('coefficients' = NDF),
    'graph' = list(), #graphnames,
    'new_data' = new_data
  )
}
