package com.mandelag.minesweeper;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet that handles the game services.
 * It will create a new game session (MineBoardGameService) if it receives a "newGame" task parameter.
 * It will return opened grids in the current session given an "open" task parameter and x y parameter.
 * @author Keenan Gebze
 */
public class GameServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String task = request.getParameter("task");
        switch (task) {
            case "newGame":
                processNewGame(request, response);
                break;
            case "open":
                processOpen(request, response);
                break;
            default:
        }
    }

    private void processNewGame(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String country = request.getParameter("country");
        int size = 400;
        if (country == null || "".equals(country)) {
            country = "Singapore";
        }
        try {
            size = Integer.parseInt(request.getParameter("grid"));
        } catch (NumberFormatException e) {
        }

        MineBoardGameService mbgs = null;
        HttpSession session = request.getSession(true);
        mbgs = new MineBoardGameService(country, size);
        session.setAttribute("mineboardgameservice", mbgs);

        response.setContentType(
                "text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.print(mbgs.getCurrentState(true));
        }
    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void processOpen(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MineBoardGameService mb = (MineBoardGameService) request.getSession().getAttribute("mineboardgameservice");
        if (mb == null) {
            sendError(request, response);
            return;
        }

        int x = Integer.parseInt(request.getParameter("x"));
        int y = Integer.parseInt(request.getParameter("y"));
        
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.print(mb.open(x, y));
        }
    }

    private void sendError(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("{\"status\":\"error\"}");
        }
    }

}
